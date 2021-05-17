#!/usr/bin/env sh
while read line; do
  result=$(echo "$line" | grep "versionCode")
  if [ "$result" != "" ]; then
    versionCode=$(echo "$line" | sed 's/[^0-9]*//g')
    echo "版本号：$versionCode"
  fi
  result=$(echo "$line" | grep "versionName")
  if [ "$result" != "" ]; then
    versionName=$(echo "$line" | cut -d "\"" -f2)
    echo "版本名：${versionName}"
    one=$(echo "$versionName" | cut -d "." -f1)
    two=$(echo "$versionName" | cut -d "." -f2)
    three=$(echo "$versionName" | cut -d "." -f3)
    if [ "$two" -lt 10 ]; then
      two="0${two}"
    fi
    if [ "$three" -lt 10 ]; then
      three="0${three}"
    fi
    versionName="$one$two$three"

  fi
done <base.gradle
echo "准备开始打包"
./gradlew assembleRelease
echo "打包完成,开始上传apk"
scp -r /Users/han.chen/punuo/PetProject/app/build/outputs/apk/release/feed_signed.apk root@39.98.36.250:/home/wwwroot/tp5/public/apk
#curl -H "Expect:" -F 'file=@/Users/han.chen/punuo/PetProject/app/build/outputs/apk/release/feed_signed.apk' http://121.5.252.108:8080/apk/upload
echo "上传完毕，更新版本信息"
curl "http://pet.qinqingonline.com/developers/appUpgrade?versionName=${versionName}&versionCode=${versionCode}"
echo "更新版本信息完成"