# JXmlIO

## このライブラリについて

XMLファイルの読み書きを行うライブラリです。
例えば、

```xml
<DATA> data
    <DATA1 bar="10"> foo</DATA1>
    <DATA2> hoge </DATA2>
</DATA >
```

のおような、XMLをコンフィグファイルとして扱うためにまとめたものです。

元はScalaで書く予定でしたが、xml周りが標準ライブラリから外れてしまったようなので、javax.xml.stream.*を使って読み込みます。

### XmlElement

xmlの各要素を格納するオブジェクトです。

### XmlElementManager

特定の要素名を持つXmlElementもしくはその内容を返すststicな関数があります。

### XmlIO

xmlを読み込んでXmlElementをリストで返すread、XmlElementをxmlに書き出すwriteがあります。

## 課題

XMLの読み込みに、javax.xml.stream.*を利用しているので、例えば、

```xml
<br />
```

のような、empty elementが読み込めないので、別のAPIに変更するか、自作するか検討したいところです。

