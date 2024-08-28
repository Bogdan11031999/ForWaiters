package com.example.forwaiters.others

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.icu.text.IDNA.Info
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import com.example.forwaiters.R
import com.example.forwaiters.database.Categoria
import com.example.forwaiters.database.TavoloPiatto
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class PDFGenerator(private val context: Context) {

    fun generateAndPrintPDF(
        piattiList: List<TavoloPiatto>,
        categoriaList: List<Categoria>,
        outputFilePath: String,
        onPdfGenerated: (File) -> Unit
    ) {
        try {
            val document = PdfDocument()
            val pageWidthMm = 1050
            val pageHeightMm = 1480
            val pageWidthPt = pageWidthMm * 2.83465f
            val pageHeightPt = pageHeightMm * 2.83465f

            val pageInfo = PdfDocument.PageInfo.Builder(pageWidthPt.toInt(), pageHeightPt.toInt(), 1).create()
            val page = document.startPage(pageInfo)
            val canvas: Canvas = page.canvas

            val paint = Paint().apply {
                color = Color.BLACK
                textSize = 170f
            }

            val paintMenu = Paint().apply {
                textSize = 100f
                color = Color.BLACK
            }

            var yOffset = 100f
            var xOffset = 20f
            val stringInfo = "${InfoOrder.piano!!.nomeAppPiano} ${InfoOrder.tavolo!!.nomeAppTavolo}"
            canvas.drawText(stringInfo, xOffset, yOffset, paintMenu)
            yOffset += 200f

            // Mappa per ordinare i piatti in base all'ordine delle categorie
            val mappaOrdineCategoria = categoriaList.associate { it.nomeAppCategoria to it.ordine }
            val piattiOrdinati = piattiList.sortedBy { mappaOrdineCategoria[it.nomeAppCategoria] ?: Int.MAX_VALUE }

            var ordineCategoriaPrecedente: Int? = null

            piattiOrdinati.forEach {
                val ordineCorrente = mappaOrdineCategoria[it.nomeAppCategoria]

                // Controlla se l'ordine della categoria è cambiato rispetto a quello precedente
                if (ordineCategoriaPrecedente != null && ordineCorrente != ordineCategoriaPrecedente) {
                    // Inserisci una separazione quando cambia l'ordine della categoria
                    canvas.drawText("-----------------", xOffset, yOffset, paint)
                    yOffset += 150f
                }

                // Stampa il piatto
                val text = if (it.quantita != 0.5) {
                    "${it.nomeAppPiatto}: ${it.quantita.toInt()}"
                } else {
                    "${it.nomeAppPiatto}: 2 x ${it.quantita}"
                }
                canvas.drawText(text, xOffset, yOffset, paint)
                yOffset += 200f

                ordineCategoriaPrecedente = ordineCorrente
            }

            document.finishPage(page)
            val file = File(outputFilePath)
            val fileOutputStream = FileOutputStream(file)
            document.writeTo(fileOutputStream)
            document.close()

            fileOutputStream.flush()
            fileOutputStream.close()

            // Chiamata callback una volta che il PDF è stato generato
            onPdfGenerated(File(outputFilePath))

        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}





object PDFPrinter {

    fun printPDF(context: Context?, file: File?) {
        if (context == null || file == null || !file.exists()) {
            return
        }

        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager?
        printManager?.let {
            val jobName = context.getString(R.string.app_name) + " Document"

            it.print(jobName, PDFPrintDocumentAdapter(file), null)
        }
    }

    private class PDFPrintDocumentAdapter(private val pdfFile: File) : PrintDocumentAdapter() {

        override fun onLayout(
            oldAttributes: PrintAttributes?,
            newAttributes: PrintAttributes?,
            cancellationSignal: CancellationSignal?,
            callback: LayoutResultCallback?,
            extras: Bundle?
        ) {
            if (cancellationSignal?.isCanceled == true) {
                callback?.onLayoutCancelled()
                return
            }

            val builder = PrintDocumentInfo.Builder("document.pdf")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)

            val info = builder.build()

            callback?.onLayoutFinished(info, !newAttributes?.equals(oldAttributes)!!)
        }

        override fun onWrite(
            pages: Array<out PageRange>?,
            destination: ParcelFileDescriptor?,
            cancellationSignal: CancellationSignal?,
            callback: WriteResultCallback?
        ) {
            if (cancellationSignal?.isCanceled == true) {
                callback?.onWriteCancelled()
                return
            }

            try {
                val input = FileInputStream(pdfFile)
                val output = FileOutputStream(destination?.fileDescriptor)

                val buffer = ByteArray(3024)
                var bytesRead: Int

                while (input.read(buffer).also { bytesRead = it } != -1) {
                    output.write(buffer, 0, bytesRead)
                }

                callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))

                input.close()
                output.close()
            } catch (e: IOException) {
                callback?.onWriteFailed(e.toString())
            }
        }
    }
}