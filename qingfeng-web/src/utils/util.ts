export function timeFix () {
  const time = new Date()
  const hour = time.getHours()
  return hour < 9 ? '早上好' : hour <= 11 ? '上午好' : hour <= 13 ? '中午好' : hour < 20 ? '下午好' : '晚上好'
}

export function welcome () {
  const arr = ['休息一会儿吧', '准备吃什么呢?', '要不要打一把 DOTA', '我猜你可能累了']
  const index = Math.floor(Math.random() * arr.length)
  return arr[index]
}

export function randomNum(len, radix) {
  const chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('')
  const uuid = []
  radix = radix || chars.length

  if (len) {
    // Compact form
    for (let i = 0; i < len; i++) uuid[i] = chars[0 | Math.random() * radix]
  } else {
    // rfc4122, version 4 form
    let r

    // rfc4122 requires these characters
    uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-'
    uuid[14] = '4'

    // Fill in random data.  At i==19 set the high bits of clock sequence as
    // per rfc4122, sec. 4.1.5
    for (let i = 0; i < 36; i++) {
      if (!uuid[i]) {
        r = 0 | Math.random() * 16
        uuid[i] = chars[(i === 19) ? (r & 0x3) | 0x8 : r]
      }
    }
  }
  return uuid.join('') + new Date().getTime()
}

/**
 * 触发 window.resize
 */
export function triggerWindowResizeEvent () {
  const event: any = new CustomEvent("resize", {"bubbles":true, "cancelable":true});
  event.eventType = 'message'
  window.dispatchEvent(event)
}

export function handleScrollHeader (callback) {
  let timer: NodeJS.Timeout

  let beforeScrollTop = window.scrollY
  callback = callback || function () {}
  window.addEventListener(
    'scroll',
    event => {
      clearTimeout(timer)
      timer = setTimeout(() => {
        let direction = 'up'
        const afterScrollTop = window.scrollY
        const delta = afterScrollTop - beforeScrollTop
        if (delta === 0) {
          return false
        }
        direction = delta > 0 ? 'down' : 'up'
        callback(direction)
        beforeScrollTop = afterScrollTop
        return
      }, 50)
    },
    false
  )
}

export function isIE () {
  const bw = window.navigator.userAgent
  const compare = (s) => bw.indexOf(s) >= 0
  const ie11 = (() => 'ActiveXObject' in window)()
  return compare('MSIE') || ie11
}

/**
 * Remove loading animate
 * @param id parent element id or class
 * @param timeout
 */
export function removeLoadingAnimate (id = '', timeout = 1500) {
  if (id === '') {
    return
  }
  setTimeout(() => {
    const element=document.getElementById("id");
    element && document.body.removeChild(element)
  }, timeout)
}
export function scorePassword (pass) {
  let score = 0
  if (!pass) {
    return score
  }
  // award every unique letter until 5 repetitions
  const letters = {}
  for (let i = 0; i < pass.length; i++) {
      letters[pass[i]] = (letters[pass[i]] || 0) + 1
      score += 5.0 / letters[pass[i]]
  }

  // bonus points for mixing it up
  const variations = {
      digits: /\d/.test(pass),
      lower: /[a-z]/.test(pass),
      upper: /[A-Z]/.test(pass),
      nonWords: /\W/.test(pass)
  }

  let variationCount = 0
  for (const check in variations) {
      variationCount += (variations[check] === true) ? 1 : 0
  }
  score += (variationCount - 1) * 10

  return parseInt(String(score))
}

export const getWeek = (week: number, useZhou) => {
  let txt = ''
  switch (week) {
    case 1:
      txt = '一'
      break
    case 2:
      txt = '二'
      break
    case 3:
      txt = '三'
      break
    case 4:
      txt = '四'
      break
    case 5:
      txt = '五'
      break
    case 6:
      txt = '六'
      break
    case 0:
      txt = '日'
      break
    default:
      return 'getWeekError'
  }
  return useZhou ? '周' : '星期' + txt
}
// aes encryption key
export const encryptKeys = {
  // key最少4位,否则报错
  key: '1111',
  iv: '1',
};


export function formatDate(date_time) {
  // var now = new Date(date_time)
  var now = new Date(parseInt(date_time));// 时间戳为10位需*1000，时间戳为13位的话不需乘1000
  var year=now.getFullYear();
  var month=now.getMonth()+1;
  if(month<10){
      month="0"+month;
  }
  var date=now.getDate();
  if(date<10){
      date="0"+date;
  }
  var hour=now.getHours();
  if(hour<10){
      hour="0"+hour;
  }
  var minute=now.getMinutes();
  if(minute<10){
      minute="0"+minute;
  }
  var second=now.getSeconds();
  if(second<10){
      second="0"+second;
  }
  return year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second;
}


export function formatCtsDate(strDate) {
  if(null==strDate || ""==strDate){
      return "";
  }
  var dateStr=strDate.trim().split(" ");
  var strGMT = dateStr[0]+" "+dateStr[1]+" "+dateStr[2]+" "+dateStr[5]+" "+dateStr[3]+" GMT+0800";
  var date = new Date(Date.parse(strGMT));
  var y = date.getFullYear();
  var m = date.getMonth() + 1;
  m = m < 10 ? ('0' + m) : m;
  var d = date.getDate();
  d = d < 10 ? ('0' + d) : d;
  var h = date.getHours();
  var minute = date.getMinutes();
  minute = minute < 10 ? ('0' + minute) : minute;
  var second = date.getSeconds();
  second = second < 10 ? ('0' + second) : second;

  return y+"-"+m+"-"+d+" "+h+":"+minute+":"+second;
};



export function getChildNum(id,data) {
  //设置结果
  let result:any =0;
  if (!data) {
    return;//如果data传空，直接返回
  }
  for (var i = 0; i < data.length; i++) {
    let item = data[i];
    if (item.id == id) {
      if (item.children){
        result = item.children.length;
      }
      return result;
    } else if (item.children && item.children.length > 0) {
      //如果有子集，则把子集作为参数重新执行本方法
      result= getChildNum(id, item.children);
      //关键，千万不要直接return本方法，不然即使没有返回值也会将返回return，导致最外层循环中断，直接返回undefined,要有返回值才return才对
      if(result){
        return result;
      }
    }
  }
  //如果执行循环中都没有return，则在此return
  return result;
}


export function formatDateTZ (timestamp, formatLayout = 'Y-m-d H:i:s') {
  let formatDate = ""
  formatLayout = formatLayout.toUpperCase()
  timestamp = (timestamp+"").length > 11 ? timestamp : timestamp * 1000
  let time = new Date(timestamp)
  for (let i in formatLayout) {
    if (['Y','M','D', 'W','H','I','S'].indexOf(formatLayout[i]) >= 0) {
      switch (formatLayout[i]) {
        case 'Y':
          formatDate += time.getFullYear()
          break;
        case 'M':
          formatDate += time.getMonth() >= 9 ? time.getMonth() + 1 : '0' + (time.getMonth() + 1)
          break;
        case 'D':
          formatDate += time.getDate() > 9 ? time.getDate() : '0' + time.getDate()
          break;
        case 'W':
          formatDate += time.getDay() == 0 ? 7 : time.getDay()
          break;
        case 'H':
          formatDate += time.getHours() > 9 ? time.getHours() : '0' + time.getHours()
          break;
        case 'I':
          formatDate += time.getMinutes() > 9 ? time.getMinutes() : '0' + time.getMinutes()
          break;
        case 'S':
          formatDate += time.getSeconds() > 9 ? time.getSeconds() : '0' + time.getSeconds()
          break;
      }
    }else{
      formatDate += formatLayout[i]
    }
  }
  return formatDate
}


export function getGuid() {
    // 创建一个 Date 对象，它将包含当前日期和时间
    var currentDate = new Date();
    // 获取年份
    var year = currentDate.getFullYear();
    // 获取月份（注意，月份从 0 开始，所以需要加 1）
    var month = currentDate.getMonth() + 1;
    // 获取日期
    var day = currentDate.getDate();
    // 获取小时
    var hours = currentDate.getHours();
    // 获取分钟
    var minutes = currentDate.getMinutes();
    // 获取秒钟
    var seconds = currentDate.getSeconds();

    // 格式化月、日、时、分和秒，以确保它们都至少有两位数字
    if (month < 10) {
      month = "0" + month;
    }
    if (day < 10) {
      day = "0" + day;
    }
    if (hours < 10) {
      hours = "0" + hours;
    }
    if (minutes < 10) {
      minutes = "0" + minutes;
    }
    if (seconds < 10) {
      seconds = "0" + seconds;
    }
    // 创建一个表示当前时间的字符串
    var currentTime = year +""+ month +day + hours + minutes + seconds;
    var DataInfo = String(currentTime);
    return DataInfo
  }