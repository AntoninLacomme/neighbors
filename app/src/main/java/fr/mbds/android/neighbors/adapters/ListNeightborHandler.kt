package fr.mbds.android.neighbors.adapters

import fr.mbds.android.neighbors.models.Neighbor

interface ListNeightborHandler {
    fun onDeleteNeibor(neighbor: Neighbor)
}