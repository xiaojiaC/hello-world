# jQuery选择器

## 元素选择器

- `div`: 选取`<div>`元素

## 基本选择器

- `#id`: id选择器
- `.class`: 类选择器
- `element`: 元素选择器
- `*`: 通配选择器
- `selector1, selector2, ..., selectorN`: 群组选择器

## 层次选择器

- `$('ancestor descendant')`: ancestor元素内的所有descendant(后代)元素
- `$('parent > child')`: parent元素内的所有child(子)元素
- `$('prev + next')`: 紧邻在prev元素后的next元素
- `$('prev ~ siblings')`: prev元素后的所有siblings(兄弟)元素

## 过滤选择器
### 基本过滤选择器

- `:first`: 取第一个元素
- `:last`: 取最后一个元素
- `:not(selector)`: 去除所有与给定选择器匹配的元素
- `:even`: 取索引是偶数的所有元素，索引从0开始
- `:odd`: 取索引是奇数的所有元素，索引从0开始
- `:eq(index)`: 取索引等于index的元素
- `:gt(index)`: 取索引大于index的元素
- `:lt(index)`: 取索引小于index的元素
- `:header`: 取所有的标题元素
- `:animated`: 取当前正在执行动画的所有元素
- `:focuse`: 取当前获取焦点的元素

### 内容过滤选择器

- `:contains(text)`: 选取文本内容含'text'的元素
- `:empty`: 选取不包含子元素或文本的空元素
- `:has(selector)`: 选取含有选择器所匹配元素的元素
- `:parent`: 选取含有子元素或文本的元素

### 可见性过滤选择器

- `:hidden`: 选取所有不可见的元素
- `:visible`: 选取所有可见的元素

### 属性过滤选择器

- `[attribute]`: 选取含有此属性的元素
- `[attribute=value]`: 选取属性值为value的元素
- `[attribute!=value]`: 选取属性值不等于value的元素
- `[attribute^=value]`: 选取属性值以value开始的元素
- `[attribute$=value]`: 选取属性值以value结束的元素
- `[attribute*=value]`: 选取属性值含有value的元素
- `[attribute|=value]`: 选取属性值等于value或以value为前缀(value后跟一个连字符"-")的元素
- `[attribute~=value]`: 选取属性用空格分割的值中包含value的元素
- `[attribute1][attribute2][attributeN]`: 用属性选择器合并成一个复合属性选择器，满足多个条件。每选择一个，缩小一次范围

### 子元素过滤选择器

- `:nth-child(index/even/odd/equation)`: 选取每个父元素下的第index个元素或者奇偶元素(index从1开始)
- `:first-child`: 选取每个父元素的第一个子元素 
- `:last-child`: 选取每个父元素的最后一个子元素 
- `:only-child`: 如果某个元素是父元素中唯一的子元素，那么将被匹配。如果父元素中含有其他子元素，则不会被匹配 

### 表单对象属性过滤选择器

- `:enabled`: 选取所有可用元素
- `:disabled`: 选取所有不可用元素
- `:checked`: 选取所有被选中元素(单选框，复选框)
- `:selected`: 选取所有被选中的选项元素(下拉列表)

## 表单选择器

- `:input`: 选取所有的`<input>`,`<textarea>`, `<select>`和`<button>`元素
- `:text`: 选取所有的单行文本框
- `:password`: 选取所有的密码框
- `:radio`: 选取所有的单选框
- `:checkbox`: 选取所有的复选框
- `:submit`: 选取所有的提交按钮
- `:image`: 选取所有的图像按钮
- `:reset`: 选取所有的重置按钮
- `:button`: 选取所有的按钮
- `:file`: 选取所有的上传域
- `:hidden`: 选取所有的不可见元素

> [jQuery Selectors](https://api.jquery.com/category/selectors/)
