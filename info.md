没有加固，直接jadx打开，  

5.8.3.32:

漫画页面，太复杂了不好分析，
com.dragon.read.component.comic.impl.comic.ui.ComicActivity
com.dragon.read.component.comic.impl.comic.ui.ComicFragment

搜索“会员”，直接定位到几个广告判断条件，  
从判断条件进入找到方法固定返回值，com.dragon.read.ad.cartoon.d.a#a  
一个成为会员， com.dragon.read.user.h#a  
一个去全部广告， com.dragon.read.user.h#e  
