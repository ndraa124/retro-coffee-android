import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R

class CouponAdapter(var dataImage: Array<Int>, var dataTitle: Array<String>): RecyclerView.Adapter<CouponAdapter.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_coupons, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount() = dataImage.size

    override fun onBindViewHolder(holder: MyHolder, position: Int) {


        holder.bind(dataTitle[position], dataImage[position])
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(dataTitle: String, dataImage: Int) {

            itemView.findViewById<ImageView>(R.id.couponImage).setImageResource(dataImage)
            itemView.findViewById<TextView>(R.id.couponDesc).setText(dataTitle)
        }

    }
}