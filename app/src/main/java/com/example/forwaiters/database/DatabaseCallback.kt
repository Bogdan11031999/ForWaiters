package com.example.forwaiters.database

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.concurrent.Executors

class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val isDatabasePopulated = sharedPreferences.getBoolean("isDatabasePopulated", false)
        if (!isDatabasePopulated) {
            Executors.newSingleThreadExecutor().execute {
                val database = ForWaitersDatabase.getInstance(context)
                popolaPiani(database.pianoDao)
                popolaTavoli(database.tavoloDao, database.pianoDao)
                popolaCategorie(database.categoriaDao)
                inserisciPiattiAntipastiFreddi(database.piattoDao)
                inserisciPiattiCaviale(database.piattoDao)
                inserisciPiattiAntipastiCaldi(database.piattoDao)
                inserisciPiattiPrimi(database.piattoDao)
                inserisciSecondiCarne(database.piattoDao)
                inserisciSecondiPesce(database.piattoDao)
                inserisciPiattiContorni(database.piattoDao)
                inserisciPiattiBevande(database.piattoDao)
                inserisciPiattiAlco(database.piattoDao)
                inserisciPiattiChampagne(database.piattoDao)
                inserisciPiattiViniRossi(database.piattoDao)
                inserisciPiattiViniGeorgiani(database.piattoDao)
                inserisciPiattiViniBianchi(database.piattoDao)
                inserisciTuttiITavoliPiatti(
                    database.tavoloDao,
                    database.piattoDao,
                    database.tavoloPiattoDao
                )
                sharedPreferences.edit().putBoolean("isDatabasePopulated", true).apply()
            }
        }
    }

    fun popolaPiani(pianoDao: PianoDao) {
        val mappaPiani = mapOf(
            "pianoUno" to "PIANO-1",
            "pianoDue" to "PIANO-2"
        )
        mappaPiani.forEach { (nomeDB, nomeApp) ->
            val piano = Piano(nomeDBPiano = nomeDB, nomeAppPiano = nomeApp)
            pianoDao.inserisciPiano(piano)
        }
    }

    fun popolaTavoli(tavoloDao: TavoloDao, pianoDao: PianoDao) {
        val mappaTavoli = mapOf(
            "tavoloUno" to "TAVOLO-1",
            "tavoloDue" to "TAVOLO-2",
            "tavoloTre" to "TAVOLO-3",
            "tavoloQuattro" to "TAVOLO-4",
            "tavoloCinque" to "TAVOLO-5",
            "tavoloSei" to "TAVOLO-6",
            "tavoloSette" to "TAVOLO-7",
            "tavoloOtto" to "TAVOLO-8",
            "tavoloNove" to "TAVOLO-9",
            "tavoloDieci" to "TAVOLO-10"
        )
        val listaPiani = pianoDao.ottieniTuttiIPiani()
        listaPiani.forEach { piano ->
            mappaTavoli.forEach { (nomeDB, nomeApp) ->
                val tavolo = Tavolo(
                    nomeDBTavolo = nomeDB + piano.nomeDBPiano,
                    nomeAppTavolo = nomeApp,
                    pianoNomeDB = piano.nomeDBPiano,
                    numeroPersone = 0
                )
                tavoloDao.inserisciTavolo(tavolo)
            }
        }
    }

    fun popolaCategorie(categoriaDao: CategoriaDao) {
        val mappaCategorie = listOf(
            "ANTIPASTI FREDDI",
            "ANTIPASTI CALDI",
            "CAVIALE",
            "PRIMI PIATTI",
            "SECONDI PIATTI DI CARNE",
            "SECONDI PIATTI DI PESCE",
            "CONTORNI",
            "BOLLICINE",
            "VINI BIANCHI",
            "VINI ROSSI",
            "ALCOLICI",
            "VINI GEORGIANI"
        )
        val ordine = mapOf(
            "ANTIPASTI FREDDI" to 1,
            "CAVIALE" to 2,
            "ANTIPASTI CALDI" to 3,
            "PRIMI PIATTI" to 4,
            "SECONDI PIATTI DI CARNE" to 5,
            "SECONDI PIATTI DI PESCE" to 6,
            "CONTORNI" to 7,
            "BEVANDE" to 8,
            "ALCOLICI" to 9,
            "BOLLICINE" to 10,
            "VINI BIANCHI" to 11,
            "VINI ROSSI" to 12,
            "VINI GEORGIANI" to 13
        )
        mappaCategorie.forEach { nomeAppCategoria ->
            val ordineCategoria = ordine[nomeAppCategoria] ?: throw IllegalStateException("Ordine non trovato per $nomeAppCategoria")
            val categoria = Categoria(nomeAppCategoria = nomeAppCategoria,ordineCategoria)
            categoriaDao.inserisciCategoria(categoria)
        }
    }

    fun inserisciPiattiAntipastiFreddi(piattoDao: PiattoDao) {
        val list = listOf(
            "ANTIPASTO",
            "ANTIPASTO PER LA VODKA",
            "ARINGA PATATE E CIPOLLA",
            "INSTALA CETRIOLI POMODORO",
            "INSALATA RUSSA",
            "INVOLTINI DI MELANZANE",
            "LARDO",
            "SALMONE MARINATO",
            "SALUMI RUSSI",
            "SCIUBA"
        )
        list.forEach { nomeAppPiatto ->
            val piatto = Piatto(nomeAppPiatto = nomeAppPiatto, nomeAppCategoria = "ANTIPASTI FREDDI")
            piattoDao.inserisciPiatto(piatto)
        }
    }

    fun inserisciPiattiCaviale(piattoDao: PiattoDao) {
        val list = listOf(
            "CAVIALE NERO",
            "CAVIALE ROSSO",
            "CREPES CON CAVIALE ROSSO",
            "SPAGHETTI CAVIALE NERO",
            "SPAGHETTI CAVIALE ROSSO",
            "TARTINA CON CAVIALE NERO",
            "TARTINA CON CAVIALE ROSSO"
        )
        list.forEach { nomeAppPiatto ->
            val piatto = Piatto(nomeAppPiatto = nomeAppPiatto, nomeAppCategoria = "CAVIALE")
            piattoDao.inserisciPiatto(piatto)
        }
    }

    fun inserisciPiattiAntipastiCaldi(piattoDao: PiattoDao) {
        val list = listOf(
            "CREPES CON CARNE",
            "CREPES CON CAVIALE ROSSO",
            "CREPES FUNGHI E FORMAGGI",
            "CREPES GRANO E FUNGHI",
            "CREPES PATATE E FUNGHI",
            "CREPES PATATE SALMONE",
            "JUILIEN"
        )
        list.forEach { nomeAppPiatto ->
            val piatto = Piatto(nomeAppPiatto = nomeAppPiatto, nomeAppCategoria = "ANTIPASTI CALDI")
            piattoDao.inserisciPiatto(piatto)
        }
    }

    fun inserisciPiattiPrimi(piattoDao: PiattoDao) {
        val list = listOf(
            "BORSHCH",
            "PELMENI CON SALMONE",
            "PELMENI IN BRODO",
            "PELMENI SIBERIANI",
            "SOLIANCA",
            "SPAGHETTI CAVIALE",
            "SPAGHETTI CAVIALE ROSSO",
            "VARENKI CON FUNGHI",
            "VERENIKI CON PATATE",
            "VARENKI CON TVOROG"
        )
        list.forEach { nomeAppPiatto ->
            val piatto = Piatto(nomeAppPiatto = nomeAppPiatto, nomeAppCategoria = "PRIMI PIATTI")
            piattoDao.inserisciPiatto(piatto)
        }
    }

    fun inserisciSecondiCarne(piattoDao: PiattoDao) {
        val list = listOf(
            "ANATRA AL FORNO FARCITA",
            "FILETTO ALLA CHERRY",
            "FILETTO ALLO STROGANOFF",
            "FILETTO ALLA WORONOFF",
            "FILETTO BISMARCK",
            "FILETTO COGNAC E PEPE",
            "GOLUBTZY",
            "GRANO SARACENO E FUNGHI",
            "GRANO SARACENO E MANZO",
            "GULASCH",
            "PETTO D'ANATRA",
            "POLPETTE DELLA CASA",
            "SALSICCIA AL FORNO",
            "ZHARKOIE",
            "ZHARKOIE DI VERDURA"
        )
        list.forEach { nomeAppPiatto ->
            val piatto = Piatto(nomeAppPiatto = nomeAppPiatto, nomeAppCategoria = "SECONDI PIATTI DI CARNE")
            piattoDao.inserisciPiatto(piatto)
        }
    }

    fun inserisciSecondiPesce(piattoDao: PiattoDao) {
        val list = listOf(
            "SALMONE AL FORNO",
            "SALMONE E FUNGHI",
            "SALMONE AL MIELE",
            "SALMONE E PODKOVA",
            "SHASHLYK DI STORIONE",
            "STORIONE ALLA MOSCA"
        )
        list.forEach { nomeAppPiatto ->
            val piatto = Piatto(nomeAppPiatto = nomeAppPiatto, nomeAppCategoria = "SECONDI PIATTI DI PESCE")
            piattoDao.inserisciPiatto(piatto)
        }
    }

    fun inserisciPiattiContorni(piattoDao: PiattoDao) {
        val list = listOf(
            "CONTORNO DI GRANO SARACENO",
            "PATATE SALTA IN PADELA",
            "PURE",
            "VERDURE ALLA GRIGLIA"
        )
        list.forEach { nomeAppPiatto ->
            val piatto = Piatto(nomeAppPiatto = nomeAppPiatto, nomeAppCategoria = "CONTORNI")
            piattoDao.inserisciPiatto(piatto)
        }
    }

    fun inserisciPiattiBevande(piattoDao: PiattoDao) {
        val list = listOf(
            "СОК ЯБЛОЧНО ВИНОГРАДНЫЙ",
            "СОК ТОМАТНЫЙ",
            "МОРС КЛЮКВЕННЫЙ",
            "КОКА КОЛА",
            "ЧАЙ",
            "КОФЕ",
            "ГАЗИРОВАНАЯ",
            "НАТУРАЛЬНАЯ"
        )
        list.forEach { nomeAppPiatto ->
            val piatto = Piatto(nomeAppPiatto = nomeAppPiatto, nomeAppCategoria = "BEVANDE")
            piattoDao.inserisciPiatto(piatto)
        }
    }

    fun inserisciPiattiAlco(piattoDao: PiattoDao) {
        val list = listOf(
            "VODKA BELUGA",
            "VODKA RUSSKY STANDART",
            "VODKA RUSSKY STANDART BICCHIERE",
            "VODKA LAMPONE",
            "VODKA GRANBERRY",
            "VODKA AMARENA",
            "VODKA CON PEPPE E MIELE",
            "BIRRA ZHYGULOVSKE",
            "BIRRA BOCHKOVOE NEFILTROVANE",
            "BIRRA KABAN",
            "AMARO",
            "WHISKY",
            "RUM",
            "BRENDI",
            "COGNAC"
        )
        list.forEach { nomeAppPiatto ->
            val piatto = Piatto(nomeAppPiatto = nomeAppPiatto, nomeAppCategoria = "ALCOLICI")
            piattoDao.inserisciPiatto(piatto)
        }
    }

    fun inserisciPiattiChampagne(piattoDao: PiattoDao) {
        val list = listOf(
            "CONEGLIANO VALDOBBIADENE PS DOCG",
            "R.D.O.LEVANTE",
            "FRANCIACORTA BRUT BARONE DI ERBUSCO",
            "BILLECART-SALMON BRUT RESERVE",
            "MOET CHANDON BRUT IMPERIAL"
        )
        list.forEach { nomeAppPiatto ->
            val piatto = Piatto(nomeAppPiatto = nomeAppPiatto, nomeAppCategoria = "BOLLICINE")
            piattoDao.inserisciPiatto(piatto)
        }
    }

    fun inserisciPiattiViniRossi(piattoDao: PiattoDao) {
        val list = listOf(
            "SASSELLA VALTELLINA SUPERIORE DOCG",
            "DOLCETTO D'ALBA",
            "ETICHETTA NERA",
            "NEBBIOLO",
            "BAROLO DOCG RISERVA",
            "BARBARESCO DOCG ASILI",
            "PINOT NERO ALTO ADIGE DOC",
            "CABERNET SAUVIGNON",
            "CABERNET SAUVIGNON ½",
            "VALPOLICELLA CLASSICO DOC",
            "AMARONE DOC",
            "ROSSO DI MONTEPULCIANO",
            "CHIANTI CLASSICO DOCG",
            "CHIANTI CLASSICO RISERVA DOCG ½",
            "NOBILE DI MONTEPULCIANO RISERVA DOCG",
            "BRUNELLO DI MONTALCINO RISERVA DOCG",
            "ZEPHYRO BOLGHERI ROSSO DOC",
            "MORELLINO DI SCANSANO DOCG",
            "ANTICO SIGILLO PRIMITIVO DI MANDURIA DOP"
        )
        list.forEach { nomeAppPiatto ->
            val piatto = Piatto(nomeAppPiatto = nomeAppPiatto, nomeAppCategoria = "VINI ROSSI")
            piattoDao.inserisciPiatto(piatto)
        }
    }

    fun inserisciPiattiViniGeorgiani(piattoDao: PiattoDao) {
        val list = listOf(
            "KISI-MTSVANE",
            "RKATSITELI",
            "SAPERAVI",
            "MUKUZANI",
            "MUKUZANI BADAGONI",
            "MUKUZANI MARANI",
            "KINDZMARAULI"
        )
        list.forEach { nomeAppPiatto ->
            val piatto = Piatto(nomeAppPiatto = nomeAppPiatto, nomeAppCategoria = "VINI GEORGIANI")
            piattoDao.inserisciPiatto(piatto)
        }
    }

    fun inserisciPiattiViniBianchi(piattoDao: PiattoDao) {
        val list = listOf(
            "GAVI DI GAVI",
            "ROERO ARNEIS",
            "GEWURZTRAMINER DOC",
            "MULLER THURGAU DOC",
            "CHARDONNAY COLLIO",
            "SAUVIGNON COLLIO",
            "PINOT GRIGIO COLLIO",
            "PINOT GRIGIO COLLIO ½",
            "PIGATO DI ALBENGA R.L.P. DOC",
            "PECORINO SUPERIORE",
            "FIANO DI AVELLINO",
            "GRECO DI TUFO",
            "VERMENTINO DI SARDEGNA DOC",
            "RIBOLLA GIALLA"
        )
        list.forEach { nomeAppPiatto ->
            val piatto = Piatto(nomeAppPiatto = nomeAppPiatto, nomeAppCategoria = "VINI BIANCHI")
            piattoDao.inserisciPiatto(piatto)
        }
    }


    fun inserisciTuttiITavoliPiatti(tavoloDao: TavoloDao, piattoDao: PiattoDao, tavoloPiattoDao: TavoloPiattoDao) {
        val listaTavoli = tavoloDao.ottieniTuttiITavoli()
        val listaPiatti = piattoDao.ottieniTuttiIPiatti()
        listaTavoli.forEach { tavolo ->
            listaPiatti.forEach { piatto ->
                val tavoloPiatto = TavoloPiatto(
                    nomeDBTavoloPiatto = tavolo.nomeDBTavolo + piatto.nomeAppPiatto,
                    nomeDBTavolo = tavolo.nomeDBTavolo,
                    nomeAppPiatto = piatto.nomeAppPiatto,
                    nomeAppCategoria = piatto.nomeAppCategoria,
                    quantita = 0.0
                )
                Log.d("TP", tavolo.nomeDBTavolo + piatto.nomeAppPiatto)
                tavoloPiattoDao.inserisciTavoloPiatto(tavoloPiatto)
            }
        }
    }
}

