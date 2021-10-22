package fr.mbds.android.neighbors.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.mbds.android.neighbors.NavigationListener
import fr.mbds.android.neighbors.R
import fr.mbds.android.neighbors.adapters.ListNeighborsAdapter
import fr.mbds.android.neighbors.adapters.ListNeightborHandler
import fr.mbds.android.neighbors.data.NeighborRepository
import fr.mbds.android.neighbors.models.Neighbor

class ListNeighborsFragment : Fragment(), ListNeightborHandler {

    private lateinit var recyclerView: RecyclerView

    /**
     * Fonction permettant de définir une vue à attacher à un fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_neighbors_fragment, container, false)
        recyclerView = view.findViewById(R.id.neighbors_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        view.findViewById<FloatingActionButton>(R.id.add_neighbor).setOnClickListener {
            (activity as? NavigationListener)?.let {
                it.showFragment(AddNeighborFragment())
            }
        }
        (activity as? NavigationListener)?.let {
            it.updateTitle(R.string.title_list_neighbors)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val neighbors = NeighborRepository.getInstance().getNeighbours()
        val adapter = ListNeighborsAdapter(neighbors, this)
        recyclerView.adapter = adapter
    }

    override fun onDeleteNeibor(neighbor: Neighbor) {
        var neighbors = NeighborRepository
            .getInstance()
            .getNeighbours()
        try {
            neighbors.remove(neighbor);
            Toast.makeText(this.context, "${neighbor.name} ${context?.resources?.getString(R.string.something_wad_drop)}", Toast.LENGTH_LONG).show()
            val adapter = ListNeighborsAdapter(neighbors, this)
            recyclerView.adapter = adapter
        } catch (e: Exception) { e.printStackTrace() }
    }
}
