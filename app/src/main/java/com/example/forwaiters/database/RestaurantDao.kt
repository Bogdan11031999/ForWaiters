package com.example.forwaiters.database
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface PianoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserisciPiano(piano: Piano)

    @Query("SELECT * FROM Piano")
    fun ottieniTuttiIPiani(): List<Piano>
}

@Dao
interface TavoloDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserisciTavolo(tavolo: Tavolo)

    @Query("SELECT * FROM Tavolo")
    fun ottieniTuttiITavoli(): List<Tavolo>

    @Query("SELECT * FROM Tavolo WHERE pianoNomeDB = :nomeDBPiano")
    fun trovaTavoliPerPiano(nomeDBPiano: String): List<Tavolo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserisciTavoloPiatto(tavoloPiatto: TavoloPiatto)
    @Query("UPDATE Tavolo SET numeroPersone = :numeroPersone WHERE nomeDBTavolo = :nomeDBTavolo")
    fun impostaNumeroPersone(nomeDBTavolo: String, numeroPersone: Int)

    @Query("SELECT numeroPersone FROM Tavolo WHERE nomeDBTavolo = :nomeDBTavolo")
    fun ottieniNumeroPersone(nomeDBTavolo: String): LiveData<Int>
}

@Dao
interface CategoriaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserisciCategoria(categoria: Categoria)

    @Query("SELECT * FROM Categoria ORDER BY ordine")
    fun ottieniTutteLeCategorie(): List<Categoria>

    @Query("UPDATE Categoria SET ordine = ordine + 1 WHERE ordine >= :ordineInizio")
    fun incrementaOrdineCategorieSuccessive(ordineInizio: Int)
    @Query("DELETE FROM Categoria WHERE nomeAppCategoria = :nomeAppCategoria")
    fun eliminaCategoriaPerNome(nomeAppCategoria: String)
    @Query("UPDATE Categoria SET ordine = :nuovoOrdine WHERE nomeAppCategoria = :nomeAppCategoria")
    fun aggiornaOrdineCategoria(nomeAppCategoria: String, nuovoOrdine: Int): Int

    @Transaction
    fun inserisciConControlloOrdine(nuovaCategoria: Categoria) {
        val categorie = ottieniTutteLeCategorie()
        categorie.find { it.ordine == nuovaCategoria.ordine }?.let {
            incrementaOrdineCategorieSuccessive(nuovaCategoria.ordine)
        }
        inserisciCategoria(nuovaCategoria)
    }
}

@Dao
interface PiattoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserisciPiatto(piatto: Piatto)
    @Query("SELECT * FROM Piatto WHERE nomeAppCategoria = :nomeAppCategoria")
    fun trovaPiattiPerCategoria(nomeAppCategoria: String): List<Piatto>
    @Update
    fun aggiornaPiatto(piatto: Piatto)
    @Query("SELECT * FROM Piatto")
    fun ottieniTuttiIPiatti(): List<Piatto>
    @Query("SELECT nomeAppPiatto FROM Piatto")
    fun ottieniTuttiNomiDBPiatto(): List<String>
    @Query("DELETE FROM Piatto WHERE nomeAppPiatto = :nomeAppPiatto")
    fun eliminaPiattoPerNome(nomeAppPiatto: String)
}
@Dao
interface TavoloPiattoDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun inserisciTavoloPiatto(tavoloPiatto: TavoloPiatto)
    @Query("SELECT quantita FROM TavoloPiatto WHERE nomeDBTavoloPiatto = :nomeDBTavoloPiatto")
    fun ottieniQuantitaTavoloPiatto(nomeDBTavoloPiatto: String): LiveData<Double>
    @Query("UPDATE TavoloPiatto SET quantita = quantita + 0.5 WHERE nomeDBTavoloPiatto = :nomeDBTavoloPiatto")
    fun incrementaQuantitaDiMezzoPerTavolo(nomeDBTavoloPiatto: String)
    @Query("UPDATE TavoloPiatto SET quantita = quantita + 1 WHERE nomeDBTavoloPiatto = :nomeDBTavoloPiatto")
    fun incrementaQuantitaTavoloPiatto(nomeDBTavoloPiatto: String)
    @Query("UPDATE TavoloPiatto SET quantita = 0 WHERE nomeDBTavoloPiatto = :nomeDBTavoloPiatto")
    fun decrementaQuantitaTavoloPiatto(nomeDBTavoloPiatto: String)
    @Query("SELECT * FROM TavoloPiatto WHERE quantita > 0 AND nomeDBTavolo = :nomeDBTavolo")
    fun ottieniTavoloPiattoConQuantitaMaggioreDiZero(nomeDBTavolo: String): LiveData<List<TavoloPiatto>>
    @Query("SELECT * FROM TavoloPiatto WHERE quantita > 0 AND nomeDBTavolo = :nomeDBTavolo")
    fun ottieniTavoloPiattoConQuantitaMaggioreDiZeroNoLive(nomeDBTavolo: String): List<TavoloPiatto>
    @Query("UPDATE TavoloPiatto SET quantita = 0")
    fun resettaQuantitaTutti()
    @Query("UPDATE TavoloPiatto SET quantita = 0 WHERE nomeDBTavoloPiatto = :nomeDBTavoloPiatto")
    fun resettaQuantitaPerPiatto(nomeDBTavoloPiatto: String)
    @Query("UPDATE TavoloPiatto SET quantita = 0 WHERE nomeDBTavolo  = :nomeDBTavolo")
    fun resettaQuantitaPerTavolo(nomeDBTavolo: String)
    @Query("SELECT * FROM TavoloPiatto WHERE nomeDBTavoloPiatto = :nomeDBTavoloPiatto")
    fun ottieniTuttiITavoloPiatto(nomeDBTavoloPiatto: String): LiveData<List<TavoloPiatto>>

}



