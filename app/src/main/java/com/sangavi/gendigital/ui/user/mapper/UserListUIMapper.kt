package com.sangavi.gendigital.ui.user.mapper

import android.util.Log
import com.sangavi.gendigital.model.UserListResponseModel
import com.sangavi.gendigital.ui.user.model.UserListUIData
import javax.inject.Inject

class UserListUIMapper @Inject constructor() {

    fun map(listData : UserListResponseModel)  : UserListUIData {
        val uiModel = UserListUIData(
            id = listData.id,
            name = listData.name,
            userName = listData.username,
            email = listData.email,
            address = listData.address.suite +",\n" + listData.address.street +",\n" + listData.address.city +" - " + listData.address.zipcode,
            lat = listData.address.geo.lat,
            lng = listData.address.geo.lng,
            phone = listData.phone,
            website = listData.website,
            companyDetail = listData.company.name +",\n" + listData.company.catchPhrase +",\n" + listData.company.bs
        )

        Log.e("UI Mapper" , " Map ${uiModel.userName}")

        return uiModel
    }
}