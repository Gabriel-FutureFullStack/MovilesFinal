import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.maxiescuela.databinding.CardClaseBinding
import com.example.maxiescuela.domain.Clase

class ClaseAdapter(
    private val listaClases: List<Clase>,
    private val onEditarClick: (Clase) -> Unit,
    private val onDetallesClick: (Clase) -> Unit
) : RecyclerView.Adapter<ClaseAdapter.ClaseViewHolder>() {

    inner class ClaseViewHolder(val binding: CardClaseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaseViewHolder {
        val binding = CardClaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClaseViewHolder, position: Int) {
        val clase = listaClases[position]
        with(holder.binding) {
            tvNombre.text = "${clase.nombre} + ${clase.nombre_grado}"
            tvDocente.text = "${clase.nombres_profesor} + ${clase.ap_paterno_profesor}"
            tvHorario.text = clase.hora_inicio

            btnEditarClase.setOnClickListener { onEditarClick(clase) }
            btnDetallesClase.setOnClickListener { onDetallesClick(clase) }
        }
    }

    override fun getItemCount(): Int = listaClases.size
}
