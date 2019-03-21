# [ProGuard](https://www.guardsquare.com/en/products/proguard)



## 混淆选项


[filename]:https://www.guardsquare.com/en/products/proguard/manual/usage#filename
[package_filter]:https://www.guardsquare.com/proguard/manual/usage#filters
[attribute_filter]:https://www.guardsquare.com/en/products/proguard/manual/attributes
[class_filter]:https://www.guardsquare.com/proguard/manual/usage#filters
[file_filter]:https://www.guardsquare.com/proguard/manual/usage#filefilters

[包名称]:https://www.guardsquare.com/en/products/proguard/manual/examples#repackaging
[堆栈]:https://www.guardsquare.com/proguard/manual/examples#stacktrace
[类库]:https://www.guardsquare.com/proguard/manual/examples#library
[注解]:https://www.guardsquare.com/proguard/manual/examples#annotations
[属性]:https://www.guardsquare.com/en/products/proguard/manual/attributes
[资源文件]:https://www.guardsquare.com/proguard/manual/examples#resourcefiles


`-dontobfuscate`

不混淆输入类文件。默认情况下，应用混淆处理; 类和类成员使用新的随机短名称，但各种`-keep`选项列出的名称除外。还将删除对调试有用的内部属性，例如源文件名，变量名和行号。

`-printmapping` \[[filename]\]

为已重命名的类和类成员打印从旧名称到新名称的映射。映射将打印到标准输出或指定文件。例如，它是后续增量混淆所必需的，或者如果您想要再次理解已混淆的堆栈跟踪。仅在混淆时适用。

`-applymapping` [filename]

重用在先前的混淆运行中打印出的给定名称映射。映射文件中列出的类和类成员将再次使用已指定的名称。未被提及的类和类成员会使用新名称。映射可以引用输入类以及库类。此选项对于增量混淆非常有用，即处理现有代码段的附加组件或小补丁。如果代码的结构发生了根本变化，ProGuard可能会打印出应用映射时导致冲突的警告。您可以通过在两个混淆运行中指定选项`-useuniqueclassmembernames`来降低这种风险。该选项只允许传一个映射文件。仅在混淆时适用。

`-obfuscationdictionary` [filename]

指定一个文本文件，其中所有有效单词都可用作混淆字段和方法名称。默认情况下，像“a”、“b”等短名称会被用作混淆名称。使用混淆字典，您可以指定要保留的关键字列表，或者带有非英文字符的标识符。空格、标点符号、重复的单词和#符号后面的注释都将被忽略。请注意，混淆字典几乎不会改善混淆效果。正常的编译器可以自动替换它们，并且通过使用更简单的名称再次混淆简单地解除效果。最有用做法通常是指定已经存在于类文件中的字符串（例如“Code”），从而减少了类文件的大小。仅在混淆时适用。

`-classobfuscationdictionary` [filename]

指定一个文本文件，其中所有有效的单词都可用作混淆类名。混淆字典类似于选项`-obfuscationdictionary`。仅在混淆时适用。

`-packageobfuscationdictionary` [filename]

指定一个文本文件，其中所有有效单词都可用作混淆包名。混淆字典类似于选项`-obfuscationdictionary`。仅在混淆时适用。

`-overloadaggressively`

混淆的时候大量使用重载，多个方法名使用同一个混淆名称，只要它们的方法签名（参数和返回类型）不同。此选项可以使处理后的代码更小（并且更难以理解）。仅在混淆时适用。

即使Java语言中不允许这种重载（参见[Java 语言规范，第三版](http://docs.oracle.com/javase/specs/jls/se5.0/html/j3TOC.html)，[第8.3节](http://docs.oracle.com/javase/specs/jls/se5.0/html/classes.html#8.3)和[第8.4.5节](http://docs.oracle.com/javase/specs/jls/se5.0/html/classes.html#8.4.5)），但生成的类文件符合Java字节码规范（参见[Java 虚拟机规范，第二版](http://docs.oracle.com/javase/specs/jvms/se5.0/html/VMSpecTOC.doc.html)，[第4.5节](http://docs.oracle.com/javase/specs/jvms/se5.0/html/ClassFile.doc.html#2877)和[第4.6节](http://docs.oracle.com/javase/specs/jvms/se5.0/html/ClassFile.doc.html#1513)的第一段）。且值得注意的是，有些工具会存在问题：

- Sun的JDK 1.2.2 javac编译器在使用这样的库进行编译时会产生异常（[cfr.Bug#4216736](http://bugs.sun.com/view_bug.do?bug_id=4216736)）。您可能不应该使用此选项来处理库。
- Sun的JRE 1.4及更高版本无法使用重载的原始字段序列化对象。
- Sun的JRE 1.5上pack200工具存在重载类成员的问题。
- java.lang.reflect.Proxy类不能处理重载的方法。
- 谷歌的Dalvik VM无法处理重载的静态字段。

`-useuniqueclassmembernames`

将相同的混淆名称分配给具有相同名称的类成员，并将不同的混淆名称分配给具有不同名称的类成员（对于每个给定的类成员签名）。如果不设置这个选项，更多类成员将映射到相同的短名称，如“a”，“b”等。因此，该选项会稍微增加结果代码的大小，但它确保保存的混淆名称映射始终可以在随后的增量混淆步骤中被遵守。例如，两个不同的接口包含具有相同名称和签名的方法。如果没有这个配置，在第一次打包混淆之后，他们两个同名方法可能会被赋予不同的混淆名。如果下一次添加代码补丁的时候有一个类同时实现了这两个接口，则ProGuard必须在增量混淆步骤中为这两个方法强制实施相同的方法名称。这样就必须更改原始混淆代码，以保持生成的代码一致。在*初始混淆步骤*中使用此选项，将永远不需要这种重命名。

此选项仅在混淆时适用。事实上，如果您计划执行增量混淆，您可能希望完全避免收缩和优化，因为这些步骤可以删除或修改您的代码的某些部分，而这些部分对于以后的添加是必不可少的。

`-dontusemixedcaseclassnames`

在混淆的时候不使用大小写混用的类名。默认情况下，混淆后的类名可能同时包含大写字母和小写字母。这样生成jar包并没有什么问题。只有在大小写不敏感的系统（例如windows）上解压时，才会涉及到这个问题。因为不区分大小写，可能会导致部分文件在解压的时候相互覆盖。想在Windows上解压缩jar的开发人员可以使用此选项来关闭此行为。因此，混淆后的jars会变得稍大。仅在混淆时适用。

`-keeppackagenames` \[[package_filter]\]

不混淆指定的包名称。配置的过滤器是以逗号分隔的包名列表。包名可以包含?，\*，\*\*通配符，并且可以在前面加!否定符。仅在混淆时适用。

`-flattenpackagehierarchy` [package_name]

通过将所有重命名的包移动到单个给定的父包中来重新打包它们。如果没有指定参数或者参数为空字符串（''），那么所有的包将被移动到根包中。此选项是进一步混淆[包名称]的一个示例。它可以使处理过的代码更小，更难以理解。仅在混淆时适用。

`-repackageclasses` [package_name]

通过将所有重命名的类文件移动到单个给定包中来重新打包它们。如果没有指定参数或者参数为空字符串（''），那么完全删除包。这项配置会覆盖`-flatternpackagehierarchy`的配置。这是进一步混淆[包名称]的另一个示例。它可以使代码体积更小，并且更加难以理解。这个与废弃的配置`-defaultpackage`作用相同。仅在混淆时适用。

在包目录中查找资源文件的类如果被移动到其他地方将不再正常工作。如果出现问题，请不要使用此选项以保持包不受影响。

`-keepattributes` \[[attribute_filter]\]

指定要保留的任何可选属性。可以有一个或者多个`-keepattributes`配置项，每个配置项的可选过滤器是Java虚拟机和ProGuard支持的以逗号分隔的[属性]名称列表。属性名称中可以包含?，\*，\*\*通配符。并且可以在前面加!否定符。当混淆一个[类库]的时候，至少要保留InnerClasses, Exceptions,  Signature属性。为了跟踪[堆栈]异常信息，需要保留SourceFile和LineNumberTable属性。最后，您可能希望保留[注解]因为你的代码依赖于它们。仅在混淆时适用。

`-keepparameternames`

指定保留方法的参数名称和类型。这个选项实际上保留了调试属性LocalVariableTable和LocalVariableTypeTable的修剪版本。它在处理[类库]时非常有用。一些IDE可以使用这些信息来帮助使用库的开发人员，例如使用工具提示或自动完成。仅在混淆时适用。

`-renamesourcefileattribute` [string]

指定要放在类文件的SourceFile属性（和SourceDir属性）中的常量字符串。请注意，必须首先设置该属性，因此还必须使用`-keepattributes`指令显式保留该属性。例如，您可能希望让已处理的库和应用程序生成有用的混淆[堆栈]跟踪。仅在混淆时适用。


`-adaptclassstrings` \[[class_filter]\]

指定对应类名中的字符串常量也应进行混淆处理。如果没有过滤器，则会调整与类名对应的所有字符串常量。使用过滤器，只调整与过滤器匹配的类中的字符串常量。例如，如果您的代码包含大量引用类的硬编码字符串，并且您不想保留其名称，则可能需要使用此选项。该配置项只在混淆阶段有效，但是在压缩/优化阶段，涉及到的类会自动保留下来。

`-adaptresourcefilenames` \[[file_filter]\]

如果资源文件与某类同名，那么混淆后资源文件被命名为与之对应的类的混淆名。如果没有过滤器，则会重命名与类文件名对应的所有资源文件。使用过滤器，仅重命名匹配的文件。例如，请参阅处理[资源文件]。仅在混淆时适用。

`-adaptresourcefilecontents` \[[file_filter]\]

更新资源文件的内容。根据相应类的混淆名称（如果有），重命名资源文件中提到的任何类名。没有过滤器，所有资源文件的内容都会更新。使用过滤器，仅更新匹配的文件。使用平台的默认字符集解析和写入资源文件。您可以通过设置环境变量LANG或Java系统属性来更改此默认字符集*file.encoding*。有关示例，请参阅处理[资源文件]。仅在混淆时适用。

警告：您可能只想将此选项应用于文本文件，因为解析和调整二进制文件作为文本文件可能会导致意外问题。因此，请确保指定足够“严格”的过滤器。

TIP: [原文链接](https://www.guardsquare.com/en/products/proguard/manual/usage#obfuscationoptions)
