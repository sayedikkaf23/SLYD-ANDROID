package chat.hola.com.app.ecom.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import chat.hola.com.app.AppController
import com.ezcall.android.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kotlintestgradle.remote.model.response.ecom.homeSubCategory.CategoryDetails
import kotlinx.android.synthetic.main.dialog_home_products.rvProductsList

/*inflates the products on homepage */
class ProductListBottomSheet(private val productLists: ArrayList<CategoryDetails>) : BottomSheetDialogFragment() {

    companion object {
        var TAG: String = ProductListBottomSheet::class.java.simpleName
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_home_products, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mListAdapter = ProductsListAdapter(productLists,
            "", !AppController.getInstance().isGuest,object : ClickItem{
            override fun clickItem() {
                dialog?.dismiss()
            }

        })
        rvProductsList.adapter = mListAdapter

    }

    interface ClickItem {
        fun clickItem()
    }
}