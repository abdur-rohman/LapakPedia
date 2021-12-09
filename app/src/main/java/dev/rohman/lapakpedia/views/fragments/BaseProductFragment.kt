package dev.rohman.lapakpedia.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.rohman.lapakpedia.databinding.FragmentBaseProductBinding

class BaseProductFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentBaseProductBinding.inflate(inflater, container, false).root
    }
}