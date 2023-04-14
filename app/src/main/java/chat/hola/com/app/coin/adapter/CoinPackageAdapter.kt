package chat.hola.com.app.coin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chat.hola.com.app.coin.model.CoinPackage
import chat.hola.com.app.manager.session.SessionManager
import com.bumptech.glide.Glide
import com.ezcall.android.R

class CoinPackageAdapter(private val mContext: Context, private var mPackages: ArrayList<CoinPackage>) : RecyclerView.Adapter<CoinPackageViewHolder>() {

    private var sessionManager: SessionManager = SessionManager(mContext)
    private lateinit var clickListener: OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinPackageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_coin_package, parent, false)
        return CoinPackageViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CoinPackageViewHolder, position: Int) {
        val data = mPackages[position]
        holder.tvTitle.text = data.title
        val currencySymbol = if (data.currencySymbol != null) data.currencySymbol else sessionManager.currencySymbol
        holder.tvPrice.text = currencySymbol + "" + data.planCost
        if (data.oldPlanCost != null)
            holder.tvOldPrice.text = currencySymbol + "" + data.oldPlanCost

        Glide.with(mContext).load(data.image).asBitmap()
                .placeholder(R.drawable.ic_coins)
                .into(holder.ivImage)

//        Glide.with(mContext).load(data.backgroundImage).asBitmap()
//                .placeholder(R.drawable.coin_plan_bg)
//                .into(holder.ivImage)

        holder.itemView.setOnClickListener { clickListener.onPackageSelect(data) }
    }

    override fun getItemCount(): Int {
        return mPackages.size
    }

    fun setData(packages: ArrayList<CoinPackage>) {
        mPackages = packages
        notifyDataSetChanged()
    }

    fun setListener(onClickListener: OnClickListener) {
        this.clickListener = onClickListener
    }

    interface OnClickListener {
        fun onPackageSelect(coinPackage: CoinPackage)
    }
}