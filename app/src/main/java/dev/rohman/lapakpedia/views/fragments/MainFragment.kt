package dev.rohman.lapakpedia.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.messaging.FirebaseMessaging
import dev.rohman.lapakpedia.R
import dev.rohman.lapakpedia.databinding.FragmentMainBinding
import dev.rohman.lapakpedia.repos.remote.requests.TokenRequest
import dev.rohman.lapakpedia.utils.preferences.LocalStorage
import dev.rohman.lapakpedia.utils.showError
import dev.rohman.lapakpedia.viewmodels.MainState
import dev.rohman.lapakpedia.viewmodels.MainViewModel
import dev.rohman.lapakpedia.views.adapters.MenuPagerAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val localStorage by inject<LocalStorage>()
    private val viewModel by viewModel<MainViewModel>()
    private val firebaseMessaging by inject<FirebaseMessaging>()

    private val menuProductsId = 1
    private val menuFavoritesId = 2
    private val menuCartsId = 3
    private val menuOrdersId = 4
    private val menuProfileId = 5

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false).apply {
            val fragments = if (localStorage.role == "buyer") listOf(
                BaseProductFragment(),
                FavoritesFragment(),
                CartsFragment(),
                OrdersFragment(),
                ProfileFragment()
            ) else listOf(
                BaseProductFragment(),
                OrdersFragment(),
                ProfileFragment()
            )

            val adapter = MenuPagerAdapter(fragments, childFragmentManager, lifecycle)
            vpScreens.adapter = adapter

            bnvMenus.menu.add(
                Menu.NONE,
                menuProductsId,
                Menu.NONE,
                getString(R.string.menu_products)
            ).setIcon(R.drawable.ic_products)

            if (localStorage.role == "buyer") {
                bnvMenus.menu.add(
                    Menu.NONE,
                    menuFavoritesId,
                    Menu.NONE,
                    getString(R.string.menu_favorites)
                ).setIcon(R.drawable.ic_favorites)

                bnvMenus.menu.add(
                    Menu.NONE,
                    menuCartsId,
                    Menu.NONE,
                    getString(R.string.menu_carts)
                ).setIcon(R.drawable.ic_carts)
            }

            bnvMenus.menu.add(Menu.NONE, menuOrdersId, Menu.NONE, getString(R.string.menu_orders))
                .setIcon(R.drawable.ic_orders)

            bnvMenus.menu.add(Menu.NONE, menuProfileId, Menu.NONE, getString(R.string.menu_profile))
                .setIcon(R.drawable.ic_profile)

            vpScreens.isUserInputEnabled = false

            firebaseMessaging.token.addOnSuccessListener { viewModel.updateToken(TokenRequest(it)) }

            viewModel.state.observe(viewLifecycleOwner) {
                when (it) {
                    is MainState.SuccessUpdateToken -> localStorage.user = it.data
                    is MainState.Error -> root.showError(it.exception.message)
                }
            }

            bnvMenus.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    menuProductsId -> {
                        vpScreens.setCurrentItem(0, true)

                        return@setOnNavigationItemSelectedListener true
                    }

                    menuFavoritesId -> {
                        vpScreens.setCurrentItem(1, true)

                        return@setOnNavigationItemSelectedListener true
                    }

                    menuCartsId -> {
                        vpScreens.setCurrentItem(2, true)

                        return@setOnNavigationItemSelectedListener true
                    }

                    menuOrdersId -> {
                        vpScreens.setCurrentItem(if (localStorage.role == "buyer") 3 else 1, true)

                        return@setOnNavigationItemSelectedListener true
                    }

                    menuProfileId -> {
                        vpScreens.setCurrentItem(if (localStorage.role == "buyer") 4 else 2, true)

                        return@setOnNavigationItemSelectedListener true
                    }
                }

                false
            }
        }

        return binding.root
    }
}