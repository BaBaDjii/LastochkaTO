package ru.lastochkato.app
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
data class ServiceItem(val name:String,val intervalKm:Int,val lastKm:Int){val nextKm get()=lastKm+intervalKm}
data class HistoryItem(val name:String,val km:Int,val cost:Int)
class MainActivity:ComponentActivity(){override fun onCreate(b:Bundle?){super.onCreate(b);setContent{App()}}}
@Composable fun App(){
 var km by remember{mutableIntStateOf(292500)}; var tab by remember{mutableIntStateOf(0)}
 var services by remember{mutableStateOf(listOf(ServiceItem("Масло + масляный фильтр",10000,282500),ServiceItem("Воздушный фильтр",15000,277500),ServiceItem("Топливный фильтр",30000,262500),ServiceItem("Свечи",30000,262500)))}
 var history by remember{mutableStateOf(listOf(HistoryItem("Лямбда-зонд",292000,0),HistoryItem("Аккумулятор",292000,0),HistoryItem("Тормоза",292000,0),HistoryItem("Лобовое стекло",292000,0),HistoryItem("Ремонт порогов",292000,40000),HistoryItem("Приводной ремень + натяжной ролик",292500,0)))}
 MaterialTheme(colorScheme=darkColorScheme()){Scaffold(topBar={CenterAlignedTopAppBar(title={Text("🐦 Ласточка — ТО")})},bottomBar={NavigationBar{
  NavigationBarItem(tab==0,{tab=0},{Icon(Icons.Default.Home,null)},label={Text("Главная")})
  NavigationBarItem(tab==1,{tab=1},{Icon(Icons.Default.Build,null)},label={Text("ТО")})
  NavigationBarItem(tab==2,{tab=2},{Icon(Icons.Default.History,null)},label={Text("История")})
 }},floatingActionButton={if(tab!=0)FloatingActionButton(onClick={if(tab==1)services=services+ServiceItem("Новая операция",10000,km)else history=listOf(HistoryItem("Новое обслуживание",km,0))+history}){Icon(Icons.Default.Add,"Добавить")}}){p->
 when(tab){0->Home(km,services,Modifier.padding(p)){km+=100};1->Regulation(services,Modifier.padding(p));else->History(history,Modifier.padding(p))}
 }}}
@Composable fun Home(km:Int,ss:List<ServiceItem>,m:Modifier,onKm:()->Unit)=LazyColumn(m.fillMaxSize(),contentPadding=PaddingValues(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
 item{Card{Column(Modifier.padding(20.dp)){Text("Chevrolet Lacetti",style=MaterialTheme.typography.headlineSmall);Text("Текущий пробег");Text("\${km} км",style=MaterialTheme.typography.displaySmall);OutlinedButton(onClick=onKm){Text("+100 км")}}}}
 item{Text("Ближайшее обслуживание",style=MaterialTheme.typography.titleLarge)}
 items(ss.sortedBy{it.nextKm}){s->val left=s.nextKm-km;Card{Row(Modifier.fillMaxWidth().padding(16.dp),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text(s.name,style=MaterialTheme.typography.titleMedium);Text("Следующая замена: \${s.nextKm} км")};Text(if(left<=0)"ПОРА" else "\${left} км",style=MaterialTheme.typography.titleMedium)}}}}
}
@Composable fun Regulation(ss:List<ServiceItem>,m:Modifier)=LazyColumn(m.fillMaxSize(),contentPadding=PaddingValues(16.dp)){item{Text("Регламент",style=MaterialTheme.typography.headlineSmall)};items(ss){s->ListItem(headlineContent={Text(s.name)},supportingContent={Text("Каждые \${s.intervalKm} км • последняя отметка \${s.lastKm} км")},trailingContent={Text("\${s.nextKm} км")});HorizontalDivider()}}
@Composable fun History(hs:List<HistoryItem>,m:Modifier)=LazyColumn(m.fillMaxSize(),contentPadding=PaddingValues(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)){item{Text("История обслуживания",style=MaterialTheme.typography.headlineSmall)};items(hs){h->Card{ListItem(headlineContent={Text(h.name)},supportingContent={Text("\${h.km} км")},trailingContent={if(h.cost>0)Text("\${h.cost} ₽")})}}}
