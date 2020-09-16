package com.example.roomwordsample

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomwordsample.Room.Word
import kotlinx.android.synthetic.main.activity_main.*

/**
 * メイン画面
 * Roomに保存されたデータをAdapterを使用して表示する */
class MainActivity : AppCompatActivity() {

    private val newWordActivityRequestCode = 1
    private lateinit var wordViewModel: WordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = WordListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        wordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)
        wordViewModel.allWords.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            // アダプタ内の単語のキャッシュされたコピーを更新します。
            words?.let { adapter.setWords(it) }
        })

        /** WordDao.getWord()でid=1のやつを受け取るテスト */
//        textViewTest.text = wordViewModel.word.word // FIXME: LiveData外すとクラッシュするけど理由不明
        wordViewModel.word.observe(this, Observer { word ->
            word?.let { textViewTest.text = it.word }
        })

        // 追加ボタン
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewWordActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }

        // 削除ボタン
        // MainActivity     (View)
        // → WordViewModel  (ViewModel)
        // → WordRepository (Model)
        // → WordDao        (Room)
        // の順にたらい回しにされていく
        clear.setOnClickListener {
            wordViewModel.deleteAll() /** Room全削除 */
        }

//        textViewTest.text = wordViewModel.allWords.toString()
    }

    // NewWordActivityから返ってきた時
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(NewWordActivity.EXTRA_REPLY)?.let {
                val word = Word(0, it) // 追加する文字の情報
                wordViewModel.insert(word) /** Roomに文字追加 */
            }
        } else { // エラー文をトーストする
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }
}