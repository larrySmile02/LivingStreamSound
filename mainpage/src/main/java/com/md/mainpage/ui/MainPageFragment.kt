package com.md.mainpage.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.md.mainpage.R
import com.md.mainpage.`interface`.IMainInfo
import com.md.mainpage.adapter.CategoryAdapter
import com.md.mainpage.adapter.MainDailySupplyAdapter
import com.md.mainpage.model.MainPageModel
import com.md.mainpage.model.bean.FakeCategoryBean
import com.md.mainpage.utils.Utils
import kotlinx.android.synthetic.main.fragment_mainpage.*

const val MAIN_REC_COLUM_CATEGORY = 4
const val MAIN_REC_COLUM_DAILY=3
/**
 * @author liyue
 * created 2021/2/16
 * 主页面-首页
 * */
class MainPageFragment : Fragment(), TextView.OnEditorActionListener, TextWatcher,View.OnClickListener {
    var mainPageModel: MainPageModel?=null
    var categoryData: List<FakeCategoryBean> = ArrayList()
    var categoryAdapter: CategoryAdapter? =null
    var dailySupplyData:List<FakeCategoryBean> = ArrayList()
    var dailySupplyAdapter:MainDailySupplyAdapter?=null
    //true:当前在展示分类和每日必听，false:当前在展示搜索区
    private var isShowMain=true
    private var iInfo: IMainInfo?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_mainpage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId === EditorInfo.IME_ACTION_SEARCH || actionId === EditorInfo.IME_ACTION_UNSPECIFIED) {
            val searchWord: String = searchEdit.text.toString().trim()
            if (TextUtils.isEmpty(searchWord)) {
                Toast.makeText(activity, getString(R.string.main_input_valid_world), Toast.LENGTH_SHORT).show()
                return false
            }

            search(searchWord)
            return true
        }
        return false

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        lltMainContent.visibility=View.GONE
        lltSearchContent.visibility=View.VISIBLE
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        lltSearchTip.visibility = View.GONE
    }

    override fun afterTextChanged(s: Editable?) {
        val searchWord: String = searchEdit.text.toString().trim()
        if (TextUtils.isEmpty(searchWord)) {
            lltSearchTip.visibility = View.VISIBLE
        }

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.searchEdit ->{
                showMainContent(false)
            }
        }
    }

    private inline fun  initData() {
        mainPageModel = MainPageModel()
        categoryData = mainPageModel!!.getMainCategory()
        dailySupplyData = mainPageModel!!.getMainDailySupplyData()

    }

    private inline fun initView() {
        searchEdit.setOnEditorActionListener(this)
        searchEdit.addTextChangedListener(this)
        searchEdit.setOnClickListener(this)
        val layoutManager = GridLayoutManager(activity, MAIN_REC_COLUM_CATEGORY)
        recMainCategory.layoutManager = layoutManager
        categoryAdapter=CategoryAdapter(activity!!, categoryData)
        recMainCategory.adapter = categoryAdapter
        categoryAdapter!!.setData(categoryData)

        val dailyLayoutManager = GridLayoutManager(activity, MAIN_REC_COLUM_DAILY)
        recDailySupply.layoutManager=dailyLayoutManager
        dailySupplyAdapter= MainDailySupplyAdapter(activity!!,dailySupplyData)
        recDailySupply.adapter=dailySupplyAdapter
        dailySupplyAdapter!!.setData(dailySupplyData)
    }

    private val viewList = ArrayList<View>()
    private fun search(searchWord: String) {
        Toast.makeText(activity, searchWord, Toast.LENGTH_SHORT).show()
        viewList.add(searchEdit)
        Utils.hideSoftKeyboard(context!!, viewList)
    }

    /**
     * @param isShow true展示分类和每日必听，false展示搜索列表
     */
    private inline fun showMainContent(isShow: Boolean){
        if(isShow){
            lltMainContent.visibility=View.VISIBLE
            lltSearchContent.visibility=View.GONE
            isShowMain=true
        }else{
            lltMainContent.visibility=View.GONE
            lltSearchContent.visibility=View.VISIBLE
            isShowMain=false
        }
        iInfo?.getSearchStatus(isShowMain)
    }

    public fun setIMainInfo(iInfo : IMainInfo){
        this.iInfo=iInfo
    }

    public fun onBackPressed(){
        if(!isShowMain)
        showMainContent(true)
    }




}