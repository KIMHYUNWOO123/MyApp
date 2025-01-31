package com.woo.myapp.ui.transaction

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.domain.entity.TransactionData
import com.woo.myapp.R
import com.woo.myapp.utils.LoadingBar

@Composable
fun Transaction(
    viewModel: TransactionViewModel = hiltViewModel(),
    accessId: String,
) {
    var isBuy by remember {
        mutableStateOf(true)
    }
    LaunchedEffect(Unit) {
        viewModel.getTransactionRecord(accessId, if (isBuy) "buy" else "sell")
    }
    val transactionData by viewModel.transactionData.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState()
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.app_color1)), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.app_color2))
            ) {
                Box(
                    modifier = Modifier.padding(10.dp), contentAlignment = Alignment.TopStart
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back_new), modifier = Modifier
                            .size(30.dp)
                            .clickable(
                                interactionSource = MutableInteractionSource(), indication = null
                            ) { backPressedDispatcher?.onBackPressed() }, contentDescription = "뒤로가기", contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(), contentAlignment = Alignment.Center
                    ) {
                        Text(text = "거래 내역", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = colorResource(id = R.color.app_color4))
                    }
                }
            }
            Row {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .background(if (isBuy) colorResource(id = R.color.app_color1) else colorResource(id = R.color.app_color8))
                        .padding(15.dp)
                        .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                            isBuy = true
                            viewModel.getTransactionRecord(accessId = accessId, "buy")
                        }, contentAlignment = Alignment.Center
                ) {
                    Text("구매내역", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = if (isBuy) colorResource(id = R.color.app_color8) else colorResource(id = R.color.app_color1))
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .background(if (!isBuy) colorResource(id = R.color.app_color1) else colorResource(id = R.color.app_color8))
                        .padding(15.dp)
                        .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                            isBuy = false
                            viewModel.getTransactionRecord(accessId = accessId, "sell")
                        }, contentAlignment = Alignment.Center
                ) {
                    Text("판매내역", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = if (!isBuy) colorResource(id = R.color.app_color8) else colorResource(id = R.color.app_color1))
                }
            }
            if (!isLoading!! && transactionData != null) {
                LazyColumn(
                    modifier = Modifier
                        .background(colorResource(id = R.color.app_color1))
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    items(transactionData!!) {
                        TransactionCard(data = it, isBuy)
                        Divider(thickness = 1.dp, color = colorResource(id = R.color.app_color))
                    }
                }
            }
        }
        if (isLoading!!) {
            LoadingBar()
        }
    }
}

@Composable
fun TransactionCard(data: TransactionData, isBuy: Boolean) {
    val color = when (data.grade) {
        1 -> colorResource(id = R.color.basic)
        2, 3, 4 -> colorResource(id = R.color.bronze)
        5, 6, 7 -> colorResource(id = R.color.silver)
        else -> colorResource(id = R.color.gold)
    }
    val textColor = when (data.grade) {
        1 -> colorResource(id = R.color.basicTextColor)
        2, 3, 4 -> colorResource(id = R.color.bronzeTextColor)
        5, 6, 7 -> colorResource(id = R.color.silverTextColor)
        else -> colorResource(id = R.color.goldTextColor)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp), contentAlignment = Alignment.Center
    ) {
        Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Row(
                Modifier
                    .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = data.img),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.fillMaxWidth(0.01f))
                Text(text = data.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = colorResource(id = R.color.app_color8))
                Spacer(modifier = Modifier.fillMaxWidth(0.01f))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(color = color)
                        .size(25.dp), contentAlignment = Alignment.Center
                ) {
                    Text(text = data.grade.toString(), color = textColor, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
            Column(Modifier.fillMaxHeight()) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .padding(end = 5.dp), contentAlignment = Alignment.CenterEnd
                ) {
                    Text(text = data.tradeDate, fontSize = 10.sp, color = colorResource(id = R.color.app_color8))
                }
                Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(end = 5.dp), contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = "${data.value} BP",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isBuy) colorResource(id = R.color.buyTextColor) else colorResource(id = R.color.sellTextColor)
                    )
                }
            }
        }
    }
}