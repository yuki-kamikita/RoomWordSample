package com.example.roomwordsample

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Word class
// Wordクラスのテーブル（エンティティ）を使用して、クラスにルームデータベースの注釈を付けます
@Database(entities = arrayOf(Word::class), version = 1, exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        // 起動時
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val wordDao = database.wordDao()

                    // Delete all content here.
                    // 全削除（そんなことしたら保存できてるかチェックできないじゃん）
                    wordDao.deleteAll()

                    // Add sample words.
                    // 文字追加
                    var word = Word(0,"Hello")
                    wordDao.insert(word)
                    word = Word(0,"World!")
                    wordDao.insert(word)
                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        // シングルトンは、データベースの複数のインスタンスが同時に開くのを防ぎます。
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): WordRoomDatabase {
            // if the INSTANCE is not null, then return it, if it is, then create the database
            // INSTANCEがnullでない場合はそれを返し、それがnullの場合はデータベースを作成します
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_database"
                )
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}