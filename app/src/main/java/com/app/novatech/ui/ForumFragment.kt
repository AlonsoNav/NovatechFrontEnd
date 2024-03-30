package com.app.novatech.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.app.novatech.R
import com.app.novatech.model.Message
import com.app.novatech.util.Database
import com.app.novatech.adapters.MessagesAdapter

class ForumFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var mensajeAdapter: MessagesAdapter? = null
    private val db = Database()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerViewPosts)
        cargarForoGeneral()
    }

    private fun cargarForoGeneral() {
        /*val endpoint = "foros/general" // Asegúrate de que este endpoint sea correcto
        db.getRequestToApi(endpoint) { response ->
            if (response.isSuccessful) {
                val responseData = response.body?.string() // Obtiene el cuerpo de la respuesta como String
                // Necesitarás convertir `responseData` de JSON a objetos `Mensaje`
                // Esto depende de cómo estés manejando la serialización JSON. Podrías usar Gson, por ejemplo.
                val mensajes = parseJsonToMensajes(responseData) // Este método debe ser implementado
                activity?.runOnUiThread {
                    mensajeAdapter = MessagesAdapter(mensajes)
                    recyclerView.adapter = mensajeAdapter
                }
            } else {
                Log.d("ForumFragment", "Error cargando mensajes del foro: ${response.message}")
            }
        }*/
    }
    private fun parseJsonToMensajes(jsonData: String?): List<Message> {
        return emptyList()
    }
}


/*class ForumFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forum, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ForumFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForumFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}*/