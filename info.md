没有加固，直接jadx打开，  

5.8.3.32:

漫画页面，太复杂了不好分析，
com.dragon.read.component.comic.impl.comic.ui.ComicActivity
com.dragon.read.component.comic.impl.comic.ui.ComicFragment

搜索“会员”，直接定位到几个广告判断条件，  
从判断条件进入找到方法固定返回值，com.dragon.read.ad.cartoon.d.a#a  
一个成为会员， com.dragon.read.user.h#a  
一个去全部广告， com.dragon.read.user.h#e

主页面是，  
com.dragon.read.pages.main.MainFragmentActivity

有好多场景是通过"PatchProxy"处理的，应该是类似热更新的效果，  
也就是只修改其他代码的话未来可能因为热更新补丁失效，  
但先不管，

福利标签相关都是以“LuckyDog”开头，  
不过有个单独的开关，  

