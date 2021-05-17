package ru.sberdevices.pub.demoapp.ui.smartapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.sberdevices.common.logger.Logger
import ru.sberdevices.pub.demoapp.ui.smartapp.Clothes.BEANIE
import ru.sberdevices.pub.demoapp.ui.smartapp.Clothes.BOOTS
import ru.sberdevices.pub.demoapp.ui.smartapp.Clothes.GLOVES
import ru.sberdevices.pub.demoapp.ui.smartapp.Clothes.JACKET
import ru.sberdevices.services.pub.demoapp.R
import ru.sberdevices.services.pub.demoapp.databinding.FragmentSmartappBinding

class SmartAppFragment : Fragment() {

    private val logger by Logger.lazy("SmartAppFragment")

    private val viewModel: SmartAppViewModel by viewModel()

    private lateinit var binding: FragmentSmartappBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSmartappBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.androidImageView).setOnClickListener { viewModel.sendServerAction() }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launchWhenResumed {
            viewModel.buyItems.collect { processPurchase(it) }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.clothesFlow.collect { processClothes(it) }
        }
    }

    private fun processPurchase(item: BuyItems) {
        when (item) {
            BuyItems.ELEPHANT -> {
                binding.elephantImageView.isVisible = true
            }
        }
    }

    private fun processClothes(clothes: Clothes) {
        logger.debug { "processClothes $clothes" }

        when (clothes) {
            BEANIE -> {
                binding.androidBeanieImageView.isVisible = true
            }

            GLOVES -> {
                binding.rigthMittenImageView.isVisible = true
                binding.leftMittenImageView.isVisible = true
            }

            BOOTS -> {
                binding.leftBootImageView.isVisible = true
                binding.rightBootImageView.isVisible = true
            }

            JACKET -> {
                binding.jacketImageView.isVisible = true
            }
        }
    }

    companion object {
        fun newInstance() = SmartAppFragment()
    }
}