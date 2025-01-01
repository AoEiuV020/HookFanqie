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


6.5.5.32:
vip还是com.dragon.read.user.model.VipInfoModel，但是构造方法参数变多了， 
搜“会员”，com.dragon.read.ad.cartoon.d.a 这里有漫画板块的一堆免广告判断条件，
有个 NsVipApi.IMPL.privilegeService().hasNoAdFollAllScene() 所有广告场景免广告，看着不错，但是这种接口不好hook,
有个 com.dragon.read.app.i.b 最小合规必要开关, 这个好像影响范围很大，都是良心向的，但实际就是“简单模式”，功能有限制，无法登录，
搜"gold_reverse"找到福利tag的开关，
搜”isCurrentVersionOut“去除更新提示，
搜”implements NsPrivilegeManager“找到了privilegeService实现类，
hook hasNoAdFollAllScene的话章末商品广告不会有，但会出现一个”看视频免30分钟广告“的文字链接，
搜"video_reader_ad"找到读取配置的方法，可以关闭那个看视频的文字链接，
用appiumInspector解析我的页面，找控件id再找R中这个id的引用，能找到我的页面com.dragon.read.component.biz.impl.mine.FanqieMineFragmentV2，
通过id找到我的页面会员卡片判断的是privilegeManager().canShowVipRelational()，return false去掉，
