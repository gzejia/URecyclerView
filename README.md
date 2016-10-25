# MyRecycleView-Dhf实现
1. 隐藏列表添加头部（底部）View出现的分割线
2. 解决瀑布流Item项高度差较大的情况下导致的分割线绘制错乱现象

# 重定义MyRecycleView属性使用说明
| Name | Format | Function|
|:----:|:----:| :-----|
| fixSize | boolean | 设置true提升性能，默认true |
| spanCount | integer | 网格以及流布局显示最大列数，默认2 |
| divider | reference | 分割符资源Id，默认没有分割符 |
| dividerLeftSpace| integer | 分隔符左边距，默认0，单位dp |
| dividerRightSpace| integer | 分隔符左边距，默认0，单位dp |
| dividerTopSpace| integer | 分隔符顶部边距，默认0，单位dp |
| dividerBottomSpace| integer | 分隔符底部边距，默认0，单位dp |
| dividerSpaceColor| color | 分隔符底部颜色，默认无 |
| dividerSpaceColor| color | 分隔符底部颜色，默认无 |
| type| flag | list：列表；grid：网格；staggeredGrid：瀑布流 |
| orientation| flag | vertical：垂直方向延伸； horizontal：水平方向延伸|
