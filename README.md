# URecyclerView 实现
1. RecyclerView 中 Adapter、ViewHolder 的使用
2. RecyclerView 展示分割符、头部与顶部 View （集合）
3. 下拉刷新, 上拉加载更多
4. 自定义上拉加载更多动画，空数据提示信息

# 预设属性值设置
| Name | Format| Function |
| :-----: | :-----: | :-----: |
| colors | reference | 下拉刷新颜色变换数组array，默认无 |
| isFixSize | boolean | 是否固定列表项视图大小，默认固定 |
| loadMoreType | enum | auto主动加载更多，autoHide主动加载更多并隐藏，click点击加载更多，clickHide点击加载更多并隐藏，默认不提供加载更多 |
| emptyType | enum | none不显示空数据提示，text显示空数据文本提示，默认none |
| spanCount | integer | 网格布局显示列数，默认2列 |
| divider_marginLeft | reference | 分隔符对应资源Drawable |
| divider_marginTop | dimension | 分隔符左边距，默认0 |
| divider_marginRight | dimension | 分隔符右边距，默认0 |
| divider_marginTop | dimension | 分隔符顶部边距，默认0 |
| divider_marginBottom | dimension | 分隔符底部边距，默认0 |
| divider_background | color | 分隔符边距底色，默认无 |
| textNetworkFail | string/reference | 网络错误提示语 |
| textDataEmpty | string/reference | 空数据内容提示语 |
| layoutManagerType | enum | list列表类型，grid网格类型，staggeredGrid，瀑布流类型 |
| orientation | enum | vertical垂直方向类型，horizontal水平方向列表 |

# 效果展示
![RecycleView基本使用v1](http://img.blog.csdn.net/20161019173301926)
![RecycleView基本使用v2](http://img.blog.csdn.net/20170213175444757?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

# 项目解析地址

RecyclerView使用攻略（助力篇） http://blog.csdn.net/gzejia/article/details/52858385

RecyclerView使用攻略（刷新篇） http://blog.csdn.net/gzejia/article/details/55007102
