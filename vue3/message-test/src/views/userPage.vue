<script setup>
import { ref, onMounted } from 'vue'
import { useCounterStore } from '@/stores/counter'
import { update } from '@/api/update'
import { useRouter } from 'vue-router'

const router = useRouter()
const store = useCounterStore()
const users = store.users
const isShow = ref(1)
const inputPassWord = ref('')
const inputName = ref('')
let lengthOfVolume = ref(50)
let Volume = ref(null)

const change = () => {
  isShow.value = 0
}

const Return = () => {
  isShow.value = 1
}

const submit = async () => {
  const sub = {
    account: users.account,
    passWord: inputPassWord.value,
    username: inputName.value,
  }
  const { data } = await update(sub)
  console.log(data)

  if (data.code === 200) {
    alert(data.message)
    store.username = inputName.value
    store.users.username = inputName.value
    store.users.passWord = inputPassWord.value
    router.push('/')
  } else {
    alert(data.message)
  }
}

/* 改变音量大小 */
const changeLengthOfVolume = (e) => {
  let left = getDistance()
  let width = e.clientX - left
  width = Math.floor(width)
  Volume.value.style.width = `${width}px`
  lengthOfVolume.value = Math.ceil((width * 100) / 230)
  store.Volume = lengthOfVolume.value * 0.01
}

/* 获取音量调整区域距离视口左侧的距离 */
const getDistance = () => {
  const element = document.getElementById('#dis')
  const rect = element.getBoundingClientRect()
  return rect.left
}

onMounted(() => {
  inputPassWord.value = users.passWord
  inputName.value = users.username
  Volume.value.style.width = `${store.Volume * 230}px`
})
</script>

<template>
  <div class="container" v-if="isShow">
    <div class="setVolume">
      <span>设置音量 :</span>
      <div class="lengthOfVolume">
        <div class="topPart" ref="Volume">
          <div class="ball"></div>
        </div>
        <div id="#dis" class="bottomPart" @click="changeLengthOfVolume"></div>
      </div>
      <div class="VolumeNum">{{ (store.Volume * 100).toFixed(0) }}</div>
    </div>
    <div class="account">
      <div>当前账号：</div>
      <div>{{ users.account }}</div>
    </div>
    <div class="account">
      <div>当前密码：</div>
      <div>{{ users.passWord }}</div>
    </div>
    <div class="account">
      <div>当前昵称：</div>
      <div>{{ users.username }}</div>
    </div>
    <button class="Button" @click="change">更改密码或昵称</button>
  </div>
  <div class="container" v-else>
    <div class="return" @click="Return">
      <img src="../assets/image/return.png" alt="" />
    </div>
    <div class="inputCss">
      <div class="inputLeft">密码</div>
      <input
        type="text"
        class="inputRight"
        placeholder="输入密码且少于20个字符"
        v-model="inputPassWord"
      />
    </div>
    <div class="inputCss">
      <div class="inputLeft">用户昵称</div>
      <input
        type="text"
        v-model="inputName"
        class="inputRight"
        placeholder="输入你想要使用的名称"
      />
    </div>
    <button class="submit" @click="submit">提交</button>
  </div>
</template>

<style scoped>
.container {
  width: 500px;
  height: 500px;
  margin: 100px auto;
  border: 1px solid #000;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
  .account {
    width: 300px;
    height: 100px;
    display: flex;
    justify-content: space-around;
    align-items: center;
  }
  .Button,
  .submit {
    color: white;
    width: 200px;
    height: 50px;
    text-align: center;
    line-height: 50px;
    background-color: red;
    box-shadow: 0 0 3px black;
    border-radius: 10px;
    cursor: pointer;
    &:active {
      background-color: darkred;
      box-shadow: 0 0 3px black;
      transform: translateY(1px);
    }
  }
}
.return {
  position: absolute;
  top: 20px;
  left: 20px;
  width: 50px;
  height: 50px;
  cursor: pointer;
  img {
    width: 100%;
    height: 100%;
  }
}
.inputCss {
  width: 100%;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: space-around;
  .inputLeft {
    width: 100px;
    height: 50px;
    text-align: center;
    line-height: 50px;
    font-size: 22px;
  }
  .inputRight {
    width: 300px;
    height: 30px;
    font-size: 22px;
  }
}
.submit {
  margin-top: 50px;
}
.setVolume {
  display: flex;
  justify-content: space-evenly;
  align-items: center;
  width: 100%;
  height: 100px;
}
.lengthOfVolume {
  position: relative;
  width: 270px;
  height: 30px;
  border: 1px solid #000;
  border-radius: 20px;
}
.topPart {
  pointer-events: none;
  position: absolute;
  top: 9px;
  left: 20px;
  width: 115px;
  background-color: #ffa000;
  height: 10px;
  z-index: 100;
  border-radius: 20px;
}
.bottomPart {
  position: absolute;
  top: 9px;
  left: 20px;
  width: 230px;
  height: 10px;
  background-color: #ccc;
  border-radius: 20px;
}
.ball {
  pointer-events: none;
  position: absolute;
  right: -10px;
  top: -5px;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background-color: #ffa000;
}
.VolumeNum {
  width: 20px;
}
</style>
