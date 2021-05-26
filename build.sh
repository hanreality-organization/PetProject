#!/usr/bin/env sh
while read -r line; do
  result=$(echo "$line" | grep "versionCode")
  if [ "$result" != "" ]; then
    versionCode=$(echo "$line" | sed 's/[^0-9]*//g')
    echo "版本号：$versionCode"
  fi
  result=$(echo "$line" | grep "versionName")
  if [ "$result" != "" ]; then
    versionName=$(echo "$line" | sed 's/.*\"\([0-9]*\.[0-9]*\.[0-9]*\).*/\1/g')
    echo "版本名：${versionName}"
    one=$(echo "$versionName" | sed 's/\([0-9]*\)\..*/\1/g')
    two=$(echo "$versionName" | sed 's/.*\.\([0-9]*\)\..*/\1/g')
    three=$(echo "$versionName" | sed 's/.*\.\([0-9]*\)/\1/g')
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
if [ $? -eq 0 ]; then
    echo "打包完成,开始上传apk"
    response=$(curl --location --request POST 'http://pet.qinqingonline.com/devupgrade/moveDevfile' --form 'updateFile=@"/Users/han.chen/punuo/PetProject/app/build/outputs/apk/release/feed_signed.apk"' --form 'fileName="feed_signed.apk"')
    result=$(echo "$response" | sed 's/.*success\":\([a-z]*\),.*/\1/g')
    if [ "$result" = 'true' ]; then
        echo "上传完毕，更新版本信息"
        response=$(curl -s "http://pet.qinqingonline.com/developers/appUpgrade?versionName=${versionName}&versionCode=${versionCode}")
        result=$(echo "$response" | sed 's/.*success\":\([a-z]*\),.*/\1/g')
        if [ "$result" = 'true' ]; then
            echo "版本更新成功"
            else
              echo "版本更新失败"
        fi
    fi
fi