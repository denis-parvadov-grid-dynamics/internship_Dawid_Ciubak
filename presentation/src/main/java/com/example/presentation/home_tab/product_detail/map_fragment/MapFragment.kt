package com.example.presentation.home_tab.product_detail.map_fragment

import android.os.Bundle
import android.view.View
import com.example.presentation.BaseFragment
import com.example.presentation.R
import com.example.presentation.databinding.MapFragmentBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.random.Random

class MapFragment : BaseFragment<MapFragmentBinding>(R.layout.map_fragment) {
    var mapView: MapView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.mapView
        MapsInitializer.initialize(
            requireContext(),
            MapsInitializer.Renderer.LATEST
        ) {}
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync { gMap ->
            gMap.setOnMapLoadedCallback {
                // max ranges for the maps API is [-90, 90] [-180, 180], we need to consider the
                // zoom too, that's why [-80, 80] [-170, 170] is used
                val randomLocation = LatLng(
                    Random.nextDouble(-80.0, 80.0),
                    Random.nextDouble(-170.0, 170.0)
                )
                gMap.addMarker(
                    MarkerOptions().position(randomLocation).title(randomLocation.toString())
                )
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(randomLocation, 10.0f))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }
}
