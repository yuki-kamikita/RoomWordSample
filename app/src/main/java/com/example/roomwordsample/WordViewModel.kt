package com.example.roomwordsample

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.roomwordsample.Room.Word
import com.example.roomwordsample.Room.WordRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WordRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    // LiveDataを使用してgetAlphabetizedWordsが返すものをキャッシュすると、いくつかの利点があります：
    // - データにオブザーバーを配置して（変更をポーリングする代わりに）、データが実際に変更されたときにのみUIを更新できます。
    // - リポジトリは、ViewModelによってUIから完全に分離されています。
    val allWords: LiveData<List<Word>>
    val word: LiveData<Word>

    init {
        val wordsDao = WordRoomDatabase.getDatabase(application, viewModelScope).wordDao()
        repository = WordRepository(wordsDao)
        allWords = repository.allWords
        word = repository.word
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     * 新しいコルーチンを起動して、データをブロックしない方法で挿入する
     */
    fun insert(word: Word) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(word)
    }

    // 削除処理追加
    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }
}