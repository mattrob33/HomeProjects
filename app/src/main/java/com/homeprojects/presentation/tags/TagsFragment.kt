package com.homeprojects.presentation.tags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.homeprojects.R

class TagsFragment : Fragment() {

    private lateinit var tagsViewModel: TagsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tagsViewModel =
            ViewModelProvider(this).get(TagsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_tags, container, false)
        val textView: TextView = root.findViewById(R.id.text_slideshow)
        tagsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}