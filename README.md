# jwt-sample-rsa
> 豆蔵デベロッパーサイトのブログ記事で利用しているサンプルコード

## 利用している記事
- [続・Auth0 java-jwtを使った素のJWT認証 - 今度は公開鍵方式でやってみた](https://developer.mamezou-tech.com/blogs/2022/12/31/rsa-java-jwt/)

|内容| 記事の利用箇所 |
|---|---|
| [RsaJwtProvider](./rsa-jwt-producer/)| トークンの生成実装(RsaJwtProvider) |
| [RsaJwtConsumer](./rsa-jwt-consumer/)| トークンの検証実装(RsaJwtConsumer) |
| [SimpleIdProvider](./rsa-simple-idprovider/)| JWT認証の実装 |
| [AddCalculator](./rsa-add-calculator/)| JWT認証の実装 |

## ビルドと実行
サンプルアプリのビルドにはJava17以上とMavenが必要です

### リポジトリclone
```shell
# Clone this repository
git clone https://github.com/extact-io/jwt-sample-rsa.git
```

### RsaJwtProviderのビルド
```shell
# Go into RsaJwtProvider
cd jwt-sample-rsa/rsa-jwt-producer
# build application
mvn clean package
```

### RsaJwtConsumerのビルド
```shell
# Go into RsaJwtConsumer
cd jwt-sample-rsa/rsa-consumer
# build application
mvn clean package
```

### AddCalculatorのビルド
```shell
# Go into AddCalculator
cd jwt-sample-rsa/rsa-simple-idprovider
# build application
mvn clean package
```

### SimpleIdProviderのビルド
```shell
# Go into SimpleIdProvider
cd jwt-sample-rsa/rsa-add-calculator
# build application
mvn clean package
```

## 実行
記事をの[こちら](https://developer.mamezou-tech.com/blogs/2022/12/31/rsa-java-jwt/)を参照
