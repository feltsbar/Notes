package com.kuzmin.shoppinglist.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.kuzmin.shoppinglist.R
import com.kuzmin.shoppinglist.databinding.ActivityNewNoteBinding
import com.kuzmin.shoppinglist.entities.NoteItem
import com.kuzmin.shoppinglist.fragments.NoteFragment
import com.kuzmin.shoppinglist.utils.HtmlManager
import com.kuzmin.shoppinglist.utils.TouchListener
import java.text.SimpleDateFormat
import java.util.*

class NewNoteActivity : AppCompatActivity() {
    lateinit var binding: ActivityNewNoteBinding
    private var note : NoteItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBarSettings()
        initialization()
        getAndShowNoteInAct()
        onClickColorPiker()
        actionMenuCallback()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initialization(){
        binding.colorPicker.setOnTouchListener(TouchListener())
    }

    private fun getAndShowNoteInAct(){
        val sNote = intent.getSerializableExtra(NoteFragment.NEW_NOTE_KEY)
        if(sNote != null ) {
            note = sNote as NoteItem
            binding.editTextTitle.setText(note?.title)
            binding.editTextDescription.setText(HtmlManager.getFromHTML(note?.content!!).trim())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onClickButtonSave()
                finish()
            }
            R.id.button_bold -> {
                onClickButtonBoldText()
            }
            R.id.button_color_picker -> {
                onClickButtonColor()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onClickButtonColor(){
        if(binding.colorPicker.isShown){
            val openAnim = AnimationUtils. loadAnimation(this, R.anim.close_color_picker)
            openAnim.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    binding.colorPicker.visibility = View.GONE
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
            binding.colorPicker.startAnimation(openAnim)
        } else {
            binding.colorPicker.visibility = View.VISIBLE
            val openAnim = AnimationUtils. loadAnimation(this, R.anim.open_color_picker)
            binding.colorPicker.startAnimation(openAnim)
        }
    }

    private fun setColorForSelectedText(colorId: Int) = with(binding) {
        val startPosition = editTextDescription.selectionStart
        val endPosition = editTextDescription.selectionEnd
        val styles = editTextDescription.text.getSpans(
            startPosition, endPosition, ForegroundColorSpan::class.java)

        if(styles.isNotEmpty()) editTextDescription.text.removeSpan(styles[0])

        editTextDescription.text.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this@NewNoteActivity, colorId)),
            startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        editTextDescription.text.trim() // убираем пробелы, которые ставятся автоматом
        editTextDescription.setSelection(endPosition)
    }

    private fun onClickColorPiker() = with(binding){
        colorButtonRed.setOnClickListener {
            setColorForSelectedText(R.color.red)
            onClickButtonColor()
        }
        colorButtonOrange.setOnClickListener {
            setColorForSelectedText(R.color.orange)
            onClickButtonColor()
        }
        colorButtonYellow.setOnClickListener {
            setColorForSelectedText(R.color.yellow)
            onClickButtonColor()
        }
        colorButtonGreen.setOnClickListener {
            setColorForSelectedText(R.color.green)
            onClickButtonColor()
        }
        colorButtonBlack.setOnClickListener {
            setColorForSelectedText(R.color.blue)
            onClickButtonColor()
        }
        colorButtonBlack.setOnClickListener {
            setColorForSelectedText(R.color.black)
            onClickButtonColor()
        }
    }

    private fun onClickButtonBoldText() = with(binding) {
        val startPosition = editTextDescription.selectionStart
        val endPosition = editTextDescription.selectionEnd
        val styles = editTextDescription.text.getSpans(
            startPosition, endPosition, StyleSpan::class.java)
        var boldStyle : StyleSpan? = null

        if(styles.isNotEmpty()){
            editTextDescription.text.removeSpan(styles[0])
        } else {
            boldStyle = StyleSpan(Typeface.BOLD)
        }

        editTextDescription.text.setSpan(
            boldStyle, startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        editTextDescription.text.trim() // убираем пробелы, которые ставятся автоматом
        editTextDescription.setSelection(endPosition)
    }

    private fun onClickButtonSave(){
        var editState = "new"
        val tempNote : NoteItem? =
            if(note == null) createNewNote() else {
                editState = "update"
                updateNote()
        }

        val intent = Intent().apply {
            putExtra(NoteFragment.NEW_NOTE_KEY, tempNote)
            putExtra(NoteFragment.EDIT_STATE_KEY, editState)
        }

        setResult(RESULT_OK, intent)
        finish()
    }

    private fun createNewNote(): NoteItem{
        return NoteItem(
            null,
            binding.editTextTitle.text.toString(),
            HtmlManager.convertToHTML(binding.editTextDescription.text),
            getCurrentTime(),
            ""
        )
    }

    private fun updateNote() : NoteItem? = with (binding){
        return note?.copy(
            title = editTextTitle.text.toString(),
            content = HtmlManager.convertToHTML(editTextDescription.text)
        )
    }

    // Функция возвращает текущую дату и время в указанном формате
    private fun getCurrentTime(): String{
        val timeFormat = SimpleDateFormat("hh:mm:ss - yyyy/MM/dd", Locale.getDefault())
        return timeFormat.format(Calendar.getInstance().time)
    }

    // Заполняем меню с помощью созданной разметки
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Создаем стрелку на actionBar для возврата на предыдущий экран
    private fun actionBarSettings(){
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Функция блокирует отображение меню действий, при выделении текста
    private fun actionMenuCallback(){
        val actionCallback = object : ActionMode.Callback{
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menu?.clear()
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menu?.clear()
                return true
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return true
            }

            override fun onDestroyActionMode(mode: ActionMode?) { }
        }
        binding.editTextDescription.customSelectionActionModeCallback = actionCallback
    }



}