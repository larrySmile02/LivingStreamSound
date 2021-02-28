package com.md.mainpage.ui

import android.animation.ValueAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.md.basedpc.DisplayUtil
import com.md.mainpage.R
import com.md.mainpage.interfaces.IMainInfo
import com.md.mainpage.interfaces.MainPageContract
import com.md.mainpage.adapter.CategoryAdapter
import com.md.mainpage.adapter.MainDailySupplyAdapter
import com.md.mainpage.model.MainPageModel
import com.md.mainpage.presenter.MainPagePresenter
import com.md.mainpage.utils.Utils
import com.md.network.api.Album
import com.md.network.api.Category
import com.md.network.api.ICategory
import kotlinx.android.synthetic.main.fragment_mainpage.*

const val TAG = "MainPageFragment"

//分类4列
const val MAIN_REC_COLUM_CATEGORY = 4

//每日必听3列
const val MAIN_REC_COLUM_DAILY = 3

//搜索栏的高度是固定36dp
const val HEIGHT_SEARCH = 36f

/**
 * @author liyue
 * created 2021/2/16
 * 主页面-首页
 * */
class MainPageFragment : Fragment(), TextView.OnEditorActionListener, TextWatcher, View.OnClickListener ,MainPageContract.View{
    var mainPageModel: MainPageModel? = null
    var categoryData: List<ICategory> = ArrayList()
    private var categoryAdapter: CategoryAdapter? = null
    private var dailySupplyData: List<Album> = ArrayList()
    private var dailySupplyAdapter: MainDailySupplyAdapter? = null

    //true:当前在展示分类和每日必听，false:当前在展示搜索区
    private var isShowMain = true
    private var iInfo: IMainInfo? = null
    private val mPresenter by lazy{
        MainPagePresenter()
    }

    init {
        mPresenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_mainpage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        requestData()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
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
//        lltMainContent.visibility = View.GONE
//        lltSearchContent.visibility = View.VISIBLE
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        val searchWord: String = searchEdit.text.toString().trim()
        if (!TextUtils.isEmpty(searchWord)) {
            showCancelIcon(true)
        }else{
            showCancelIcon(false)
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.searchEdit -> {
                showMainContent(false)
                showLeftSearchIcon(true)
            }

            R.id.ivCancelContainer ->{
                searchEdit.text=null
            }
        }
    }

    private inline fun initData() {
        mainPageModel = MainPageModel()

    }

    private inline fun initView() {
        searchEdit.setOnEditorActionListener(this)
        searchEdit.addTextChangedListener(this)
        searchEdit.setOnClickListener(this)
        val layoutManager = GridLayoutManager(activity, MAIN_REC_COLUM_CATEGORY)
        recMainCategory.layoutManager = layoutManager
        categoryAdapter = CategoryAdapter(activity!!, categoryData)
        recMainCategory.adapter = categoryAdapter

        val dailyLayoutManager = GridLayoutManager(activity, MAIN_REC_COLUM_DAILY)
        recDailySupply.layoutManager = dailyLayoutManager
        dailySupplyAdapter = MainDailySupplyAdapter(activity!!, dailySupplyData)
        recDailySupply.adapter = dailySupplyAdapter

        ivCancelContainer.setOnClickListener(this)
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
    private inline fun showMainContent(isShow: Boolean) {
        isShowMain = if (isShow) {
//            lltMainContent.visibility = View.VISIBLE
//            lltSearchContent.visibility = View.GONE
            if(isShowMain)return
            hideSearchZoneAnimation()
            true
        } else {
//            lltMainContent.visibility = View.GONE
//            lltSearchContent.visibility = View.VISIBLE
            if(!isShowMain)return
            showSearchZoneAnimation()
            false
        }
        iInfo?.getSearchStatus(isShowMain)
    }

    private inline fun showSearchZoneAnimation() {
        val parent = lltSearchContent.parent as FrameLayout
        val H = parent.height
        val targetHeight = H - DisplayUtil.dip2px(activity, HEIGHT_SEARCH)
        val params = lltSearchContent.layoutParams
        val valueAnimator = ValueAnimator.ofInt(0, targetHeight)
        valueAnimator.addUpdateListener { animation ->
            kotlin.run {
                Log.e(TAG, "value=" + animation.animatedValue as Int)
                params.height=animation.animatedValue as Int
                lltSearchContent.layoutParams=params

            }
        }
        valueAnimator.duration = 500
        valueAnimator.start()
        Log.e(TAG, "targetHeight=$targetHeight")
    }

    private inline fun hideSearchZoneAnimation(){
        val params = lltSearchContent.layoutParams
        val curHeight=params.height
        val valueAnimator = ValueAnimator.ofInt(curHeight, 0)
        valueAnimator.addUpdateListener { animation ->
            kotlin.run {
                Log.e(TAG, "value=" + animation.animatedValue as Int)
                params.height=animation.animatedValue as Int
                lltSearchContent.layoutParams=params

            }
        }
        valueAnimator.duration = 500
        valueAnimator.start()
    }

    private inline fun showCancelIcon(isShow: Boolean){
        if (isShow)ivCancelContainer.visibility=View.VISIBLE
        else ivCancelContainer.visibility=View.GONE
    }

    private inline fun showLeftSearchIcon(isShow: Boolean){
        if (isShow) {
            ivLeftSearchIcon.visibility = View.VISIBLE
            lltSearchTip.visibility = View.GONE
        }
        else {
            ivLeftSearchIcon.visibility= View.GONE
            lltSearchTip.visibility = View.VISIBLE
        }
    }

    public fun setIMainInfo(iInfo: IMainInfo) {
        this.iInfo = iInfo
    }

    public fun onBackPressed() {
        if (!isShowMain){
            showMainContent(true)
            showLeftSearchIcon(false)
            showCancelIcon(false)
            searchEdit.text = null
        }

    }

    private fun requestData(){
        mPresenter.getLoacalCategoryList()
        mPresenter.getDailyAlbums()
    }

//    override fun setCateDetailList(categories: ArrayList<Category>) {
//        categoryAdapter!!.setData(categories)
//    }

    override fun setCateDetailList(categories: ArrayList<ICategory>) {
        categoryAdapter!!.setData(categories as ArrayList<Category>)
    }

    override fun setDailyAlbums(albums: ArrayList<Album>) {
        dailySupplyAdapter!!.setData(albums)
        Log.e(TAG,"${albums.size}")
    }

    override fun showError(errorMsg: String) {
        Log.e(TAG,"$errorMsg")
    }


}