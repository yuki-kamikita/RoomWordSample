package com.example.roomwordsample

import androidx.lifecycle.LiveData
import com.example.roomwordsample.Room.Word
import com.example.roomwordsample.Room.WordDao

/**
 * 実際にRoomにアクセスしたいClass(ViewModel) - Repository - Room(Dao) のように繋がる？
 * 外部データベースとの接続を司るところ？
 * 今回はRoomしか使わないのであんま意味なさそう？ */
// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
// DAOをコンストラクターのプライベートプロパティとして宣言します
// DAOへのアクセスのみが必要なため、データベース全体ではなくDAOを渡す
class WordRepository(private val wordDao: WordDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    // Roomはすべてのクエリを別のスレッドで実行します。
    // Observed LiveDataは、データが変更されたときにオブザーバーに通知します。
    val allWords: LiveData<List<Word>> = wordDao.getAlphabetizedWords() /** Roomの内容をLiveDataで変数に格納 */

    val word: LiveData<Word> = wordDao.getWord()

    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }

    // 削除処理追加
    suspend fun deleteAll() {
        wordDao.deleteAll()
    }

}