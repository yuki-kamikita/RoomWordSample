package com.example.roomwordsample

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_new_word.*

/**
 * Roomに追加する時の画面 */
class NewWordActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_word)

        button_save.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(edit_word.text)) { // 入力が空
                setResult(Activity.RESULT_CANCELED, replyIntent) // MainActivityにエラーを返す
            } else { // 入力あり
                val word = edit_word.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, word)
                setResult(Activity.RESULT_OK, replyIntent) // MainActivityに入力文字を返す
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}