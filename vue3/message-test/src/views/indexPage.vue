<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { useCounterStore } from '@/stores/counter'
import { getHistory } from '@/api/getHistory'
import messageComponents from '@/components/messageComponents.vue'
import userImg from '../assets/image/user.png'
import userActiveImg from '../assets/image/user-active.png'
import a from '../../src/assets/music/music.mp4'

const store = useCounterStore()
const router = useRouter()
const activeImgPath = ref(userActiveImg) //active的user图片
const temp = ref(userImg)
const ImgPath = ref(userImg) //默认user图片
const show = ref(0) //是否展示小框
const inputValue = ref('') //输入框的具体输入值
const Message = ref([]) //储存所有消息
const socket = ref(null)
const save = ref(0)
const isButtonDisabled = ref(false) //控制按钮样式
const isGet = ref(1)
const pageRef = ref(null) // 用于获取页面根元素
const canvas = ref(null) //获取画布元素
const canvasShow = ref(0) //是否展示消息气泡
const messageNum = ref(0) //目前尚未阅读的消息数量
const audio = new Audio(a)
const liveNum = ref(0) //当前在线人数
const isLook = store.isLook

/* 方法区 */

/* 公共区域点击事件 */
const advice = () => {
  isLook.value = 0
  store.isLook = 0
}

/* 使用canvas画出消息气泡 */
const draw = () => {
  if (canvas.value.getContext) {
    var ctx = canvas.value.getContext('2d')
    // 绘制气泡形状（圆形加底部小三角形）
    const bubbleX = canvas.value.width / 2
    const bubbleY = canvas.value.height / 2 - 50
    const bubbleRadius = 20
    ctx.beginPath()
    ctx.arc(bubbleX, bubbleY, bubbleRadius, 0, 2 * Math.PI)
    ctx.closePath()

    // 设置填充颜色
    ctx.fillStyle = 'rgba(0,153,255)'
    ctx.fill()

    const triangleBase = 10 // 三角形底边长度
    const triangleHeight = 8 // 三角形高度
    const triangleX = bubbleX - triangleBase / 2 // 使三角形底边水平居中在椭圆下方
    const triangleY = bubbleY + bubbleRadius - 1 // 让三角形位于椭圆正下方
    ctx.beginPath()
    ctx.moveTo(triangleX, triangleY)
    ctx.lineTo(triangleX + triangleBase, triangleY)
    ctx.lineTo(triangleX + triangleBase / 2, triangleY + triangleHeight)
    ctx.closePath()
    ctx.fillStyle = 'rgba(0,153,255)' // 设置三角形填充颜色与椭圆一致，保持整体感
    ctx.fill()
    // 设置文字样式
    ctx.font = 'bold 24px Arial'
    ctx.textAlign = 'center'
    ctx.textBaseline = 'middle'
    ctx.fillStyle = 'white'

    // 绘制数字
    const number = messageNum.value
    ctx.fillText(number.toString(), bubbleX, bubbleY)
  }
}

/* 获取历史聊天记录 */
const getHis = async () => {
  Message.value = []
  const { data } = await getHistory()
  let temp = []
  data.forEach((jsonStr) => {
    // 去除外层单引号，将其转换为符合JSON要求的双引号包裹的字符串形式
    const validJsonStr = jsonStr.replace(/'/g, '"')
    const parsedObj = JSON.parse(validJsonStr)
    temp.unshift(parsedObj)
  })
  temp.forEach((jsonStr) => {
    Message.value.unshift(jsonStr)
  })
  isGet.value = 0
}

/* 页面滚动事件 */
window.addEventListener('scroll', function () {
  if (
    document.documentElement.scrollTop >
    document.documentElement.scrollHeight - document.documentElement.clientHeight - 150
  ) {
    canvasShow.value = 0
    messageNum.value = 0
  }
})

/* websocket长链接 */
const connectWebSocket = () => {
  socket.value = new WebSocket('ws://43.139.233.117:8080/ws')
  socket.value.onopen = () => {
    console.log('WebSocket连接已建立')
  }
  socket.value.onmessage = (event) => {
    console.log(event.data)
    const num = parseInt(event.data)
    if (num) {
      liveNum.value = num
    } else {
      const data = JSON.parse(event.data)
      audio.play()
      Message.value.push(data)
      let dis1 = document.documentElement.scrollTop
      let dis2 = document.documentElement.scrollHeight - document.documentElement.clientHeight
      if (dis1 > dis2 - 150) {
        window.scroll(0, document.documentElement.scrollHeight)
      } else {
        canvasShow.value = 1
        messageNum.value++
        nextTick(() => {
          draw()
        })
      }
    }
  }
  socket.value.onclose = (event) => {
    console.log('WebSocket连接已关闭', event)
  }
  socket.value.onerror = (event) => {
    console.log('WebSocket连接出错', event)
  }
}
//改变是否active
const changeImg = () => {
  ImgPath.value = activeImgPath.value
  show.value = 1
}
const changeImg2 = () => {
  ImgPath.value = temp.value
  show.value = 0
}

//提交输入框
const handleSubmit = async () => {
  if (inputValue.value === '' || save.value === 1) {
    return
  }
  const message = {
    message: inputValue.value,
    username: store.username,
    date: Date.now(),
  }
  if (socket.value) {
    const data = JSON.stringify(message)
    socket.value.send(data)
  }
  // 提交后可以清空输入框的值
  inputValue.value = ''
  save.value = 1
  isButtonDisabled.value = true
  window.scroll(0, document.documentElement.scrollHeight)
  setTimeout(() => {
    save.value = 0
    isButtonDisabled.value = false
  }, 1000)
}

/* const getScrollableHeight = () => {
  if (pageRef.value) {
    const clientHeight = pageRef.value.clientHeight
    const scrollHeight = pageRef.value.scrollHeight
    return scrollHeight - clientHeight
  }
  return 0
} */

/* 重新登陆代码 */
const reLogin = () => {
  store.count = 0
  store.username = ''
  store.users = ''
  router.push('/login')
}
onMounted(() => {
  connectWebSocket()
  pageRef.value = document.documentElement || document.body
  audio.load()
  audio.volume = store.Volume
})
onUnmounted(() => {
  socket.value.close()
})
</script>

<template>
  <div class="advice" v-if="!isLook">
    <div class="adviceMain">
      <div class="title">温馨提示</div>
      <div class="adviceText">
        感谢特地前来帮我测试这个聊天室如果有任何建议或者bug可以直接微信找我，账号密码会自动保存并登录，想要更换账号可以点击右上角的重新登陆
        项目源码地址：
        <a href="github">1111</a>
      </div>

      <div class="adviceConfirm" @click="advice">确定</div>
    </div>
  </div>
  <div class="bump" v-if="canvasShow">
    <canvas ref="canvas" width="200px" height="200px"></canvas>
  </div>
  <div class="header">
    <div class="right" @mouseenter="changeImg" @mouseleave="changeImg2">
      <img :src="ImgPath" alt="" class="userImg" />
      <div class="hidePart" v-if="show">
        <div class="hideText" @click="reLogin">重新登陆</div>
        <div class="hideText2" @click="router.push('/user')">个人中心</div>
      </div>
    </div>
    <div class="center">
      <div class="circle"></div>
      <div class="centerText">当前有{{ liveNum }}人在线</div>
    </div>
  </div>
  <div class="container">
    <div class="history" @click="getHis" v-if="isGet">获取历史记录</div>
    <messageComponents
      v-for="item in Message"
      v-bind:key="item.id"
      :message="item.message"
      :username="item.username"
      :date="item.date"
    ></messageComponents>
  </div>
  <div class="bottom">
    <div class="inputArea">
      <textarea
        name=""
        id=""
        placeholder="请输入内容"
        v-model="inputValue"
        @keyup.enter="handleSubmit"
      ></textarea>
    </div>
    <button
      class="submitButton"
      @click="handleSubmit"
      :disabled="isButtonDisabled"
      :class="{ 'disabled-button': isButtonDisabled }"
    >
      发送
    </button>
  </div>
  <div class="bottomFixed"></div>
  <div class="bottomMask"></div>
</template>

<style scoped>
.advice {
  position: absolute;
  z-index: 50;
  width: 100%;
  height: 98vh;
  background-color: black;
  opacity: 0.8;
  display: flex;
  justify-content: center;
  align-items: center;
  .adviceMain {
    width: 500px;
    height: 500px;
    border-radius: 20px;
    background-color: white;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    .title {
      font-size: 36px;
      color: red;
    }
    .adviceText {
      width: 300px;
      height: 200px;
      padding-top: 30px;
      color: #000;
    }
  }
  .adviceConfirm {
    width: 100px;
    height: 50px;
    border-radius: 10px;
    line-height: 50px;
    color: white;
    text-align: center;
    border-radius: 5px;
    box-shadow: 0 0 5px black;
    background-color: red;
    &:active {
      background-color: darkred;
      box-shadow: 0 0 3px black;
      transform: translateY(1px); /* 按钮按下时稍微向下移动，模拟按下的感觉 */
    }
  }
}
.bump {
  position: fixed;
  bottom: 5px;
  right: 20px;
}
.header {
  position: relative;
  width: 100%;
  height: 100px;
  border-bottom: 1px solid #000;
  .right {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    position: absolute;
    right: 50px;
    bottom: 20px;
    cursor: pointer;
    .userImg {
      width: 100%;
      height: 100%;
      transition: all 0.3s ease;
    }

    .hidePart {
      position: absolute;
      top: 50px;
      left: -40px;
      z-index: 10;
      width: 120px;
      height: 70px;
      border-radius: 10px;
      border: 1px solid #000;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      background-color: #fff;
      .hideText {
        border-bottom: 1px solid #000;
        padding-bottom: 5px;
      }
      .hideText2 {
        margin-top: 5px;
      }
      .hideText:hover,
      .hideText2:hover {
        color: red;
      }
    }
  }
  .center {
    position: absolute;
    bottom: 30%;
    left: 40%;
    display: flex;
    align-items: center;
    .circle {
      width: 15px;
      height: 15px;
      border-radius: 50%;
      background-color: green;
    }
  }
}

.container {
  .history {
    width: 100%;
    text-align: center;
    cursor: pointer;
    &:hover {
      color: #007bff;
    }
  }
  width: 80%;
  margin: 0 auto;
}

.bottom {
  position: fixed;
  z-index: 2;
  bottom: 50px;
  width: 600px;
  left: 0;
  right: 0;
  height: 60px;
  margin: 0 auto;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  background-color: #f5f5f5;

  .inputArea {
    flex: 1;
    height: 40px;
    border-radius: 25px;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #fff;
    box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.1);

    textarea {
      width: 100%;
      height: 25px;
      border: none;
      resize: none;
      outline: none;
      cursor: auto;
      overflow-x: hidden;
      overflow-y: auto;
      padding: 0 20px;
      border-radius: 25px;
      font-size: 20px;
      color: #333;
    }
  }

  .submitButton {
    width: 80px;
    height: 40px;
    border: none;
    border-radius: 25px;
    background-color: #007bff;
    color: #fff;
    font-size: 14px;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }

  .submitButton:hover {
    background-color: #0056b3;
  }
}
.disabled-button {
  background-color: #ccc !important;
  cursor: not-allowed !important;
  opacity: 0.6;
}
.bottomFixed {
  width: 100%;
  height: 250px;
}
.bottomMask {
  position: fixed;
  bottom: 0;
  width: 100%;
  height: 130px;
  background-color: #fff;
  z-index: 1;
}
</style>
