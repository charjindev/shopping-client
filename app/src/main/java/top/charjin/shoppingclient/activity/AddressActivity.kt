package top.charjin.shoppingclient.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.address_activity_main.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import top.charjin.shoppingclient.R
import top.charjin.shoppingclient.adapter.AddressAdapter
import top.charjin.shoppingclient.entity.OsAddress
import top.charjin.shoppingclient.entity.OsUser
import top.charjin.shoppingclient.utils.HttpUtil
import top.charjin.shoppingclient.utils.JsonUtil
import top.charjin.shoppingclient.utils.Router
import java.io.IOException

class AddressActivity : AppCompatActivity(), Callback {
    private lateinit var user: OsUser
    private lateinit var adapter: AddressAdapter

    private val addressList = mutableListOf<OsAddress>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.address_activity_main)

        // 通过 application 对象获取用户对象, 此处简单处理
        user = OsUser()
        user.userId = 1

        // 初始化地址信息
        adapter = AddressAdapter(this, addressList)
        rv_address.layoutManager = LinearLayoutManager(this)
        rv_address.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rv_address.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        // 在onResume中进行数据请求以避免从另一页面修改地址后转回而数据不更新
        addressList.clear()
        HttpUtil.sendOkHttpRequestByGet(Router.ADDRESS_URL + "getAllAddressByUserId?userId=1", this)
    }

    fun finishOnClick(view: View) = finish()

    fun addNewAddress(view: View) {
        val intent = Intent(this, AddressModifyActivity::class.java)
        intent.putExtra("addressOperatorType", AddressModifyActivity.ADDRESS_ADD)   // 0 : add, 1 : update

        val address = OsAddress()
        address.userId = user.userId

        intent.putExtra("address", address) //传递空数据的的Address地址对象
        startActivity(intent)
    }

    override fun onFailure(call: Call, e: IOException) {
        runOnUiThread {
            Toasty.error(this@AddressActivity, "地址数据访问失败!").show()
        }
    }

    override fun onResponse(call: Call, response: Response) {
        val jsonData = response.body()!!.string()
        val data = JsonUtil.parseJSONObjectInStringToEntityList(jsonData, OsAddress::class.java)

        addressList.addAll(data)
        runOnUiThread {
            adapter.notifyDataSetChanged()
        }
    }
}