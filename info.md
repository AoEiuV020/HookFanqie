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

5.8.5.18:

小说阅读页是，  
com.dragon.read.reader.ReaderActivity

章节末尾的广告判断在，  
com.dragon.read.ad.b.c.a#a

搜索"看视频免30分钟广告"定位到激励视频入口在，  
com.dragon.read.ad.j#a  
改这个属性可以修改广告类型，但似乎无法关闭，  
com.dragon.read.reader.ad.model.l$a#d

跟踪时找到"video_reader_ad"，进一步找到读取配置的方法，  
com.dragon.read.base.ad.a#a  
这里有好多个带ad的属性，都改成false关闭，应该可以关闭一些广告，  


