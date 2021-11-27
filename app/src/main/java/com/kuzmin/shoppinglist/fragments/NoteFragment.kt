package com.kuzmin.shoppinglist.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuzmin.shoppinglist.activities.MainApp
import com.kuzmin.shoppinglist.activities.NewNoteActivity
import com.kuzmin.shoppinglist.database.MainViewModel
import com.kuzmin.shoppinglist.database.NoteAdapter
import com.kuzmin.shoppinglist.databinding.FragmentNoteBinding
import com.kuzmin.shoppinglist.entities.NoteItem

class NoteFragment : BaseFragment(), NoteAdapter.Listener {
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var editLauncher: ActivityResultLauncher<Intent>
    private lateinit var adapter : NoteAdapter
    private val mainViewModel : MainViewModel by activityViewModels{
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    companion object {
        const val NEW_NOTE_KEY = "title_key"
        const val EDIT_STATE_KEY = "edit_state_key"
        @JvmStatic
        fun newInstance() = NoteFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onEditResult()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observer()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




    override fun onClickNew() {
        editLauncher.launch(Intent(activity, NewNoteActivity::class.java))
    }

    // При нажатии на элемент заметок в списке
    override fun onClickItem(note: NoteItem) {
        val intent = Intent(activity, NewNoteActivity::class.java).apply {
            putExtra(NEW_NOTE_KEY, note)
        }
        editLauncher.launch(intent)
    }

    // Инициализируем RecyclerView, присваиваем адаптер
    private fun initRecyclerView() = with(binding){
        recyclerViewNote.layoutManager = LinearLayoutManager(activity)
        adapter = NoteAdapter(this@NoteFragment)
        recyclerViewNote.adapter = adapter
    }

    // Создаем наблюдателя изменений, в качестве владельца указываем наш фрагмент
    // в качестве объекта наблюдения указываем наш адаптер, в котором находится список
    private fun observer(){
        mainViewModel.allNotes.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    // Получаем результат создания новой заметки как NoteItem в качестве Intent с NewNoteActivity
    private fun onEditResult(){
        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                val editState = it.data?.getStringExtra(EDIT_STATE_KEY)
                if(editState == "update"){
                    mainViewModel.updateNote(it.data?.getSerializableExtra(NEW_NOTE_KEY)
                        as NoteItem)
                } else {
                    mainViewModel.insertNote(it.data?.getSerializableExtra(NEW_NOTE_KEY)
                        as NoteItem)
                }
            }
        }
    }

    override fun deleteItem(id: Int) {
        mainViewModel.deleteNote(id)
    }


}