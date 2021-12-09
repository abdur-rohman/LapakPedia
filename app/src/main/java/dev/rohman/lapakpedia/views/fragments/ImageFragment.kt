package dev.rohman.lapakpedia.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import coil.load
import dev.rohman.lapakpedia.databinding.FragmentImageBinding

class ImageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentImageBinding.inflate(inflater, container, false).apply {
            ivImage.load(arguments?.getString(IMAGE_URL))
        }.root
    }

    companion object {
        private const val IMAGE_URL = "IMAGE_URL"

        @JvmStatic
        fun newInstance(image: String) =
            ImageFragment().apply { arguments = bundleOf(IMAGE_URL to image) }
    }
}