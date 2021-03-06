package com.example.roomwordsample.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * SQLiteのテーブル名、カラムの定義 */
@Entity(tableName = "word_table") // SQLiteのtableの定義
class Word(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "word") val word: String
)
