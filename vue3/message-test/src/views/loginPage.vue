<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useCounterStore } from '@/stores/counter'
import { query } from '@/api/query'

/* 数据区 */
const account = ref('')
const passWord = ref('')
const router = useRouter()
const store = useCounterStore()
const isLog = ref(0)

/* 方法区 */

const login = async () => {
  if (account.value === '' || passWord.value === '') {
    alert('用户名或账号不能为空')
  } else {
    isLog.value = await loginQuery()
    console.log(isLog.value)

    if (!isLog.value) {
      return
    }
    store.count = 1
    store.username = account.value
    router.push({
      path: '/',
      query: {
        id: account.value,
      },
    })
    account.value = ''
    passWord.value = ''
  }
}
const loginQuery = async () => {
  const users = {
    account: account.value,
    passWord: passWord.value,
  }
  const { data } = await query(users)
  console.log(data)

  if (data.code === 200) {
    store.users = data.users
    store.username = data.users.username
    return 1
  } else {
    alert(data.message)
    return 0
  }
}

const press = async () => {
  login()
}
</script>

<template>
  <div class="container">
    <div class="mainLeft">
      <img src="../assets/image/loginImg.png" alt="" class="mainImg" />
    </div>
    <div class="mainRight">
      <div class="rightText">系统名字</div>
      <div class="Input" @keydown.enter="press">
        <div>账号：</div>
        <input type="text" v-model="account" />
      </div>
      <div class="Input" @keydown.enter="press">
        <div>密码：</div>
        <input type="password" v-model="passWord" />
      </div>
      <div class="bottom">
        <div class="buttonPart" @click="login">
          <div class="buttonLogin">
            <div>登陆</div>
          </div>
        </div>
        <div class="register" @click="router.push('/register')">
          <div>注册</div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  .mainLeft {
    height: 400px;
    .mainImg {
      height: 100%;
    }
  }
  .mainRight {
    border: 1px solid #ccc;
    border-radius: 5px;
    width: 400px;
    height: 400px;
    display: flex;
    flex-direction: column;
    justify-content: space-evenly;
    align-items: center;
    .rightText {
      height: 50px;
      line-height: 50px;
    }
    .Input {
      display: flex;
      justify-content: space-around;
      align-items: center;
    }
    .bottom {
      display: flex;
      align-items: center;
      justify-content: space-around;
      width: 100%;
      .buttonPart {
        cursor: pointer;
        .buttonLogin {
          color: white;
          height: 50px;
          width: 150px;
          line-height: 50px;
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
      .register {
        cursor: pointer;
        color: white;
        background-color: greenyellow;
        box-shadow: 0 0 5px green;
        height: 50px;
        width: 150px;
        line-height: 50px;
        text-align: center;
        border-radius: 5px;
        &:active {
          background-color: green;
          box-shadow: 0 0 3px black;
          transform: translateY(1px); /* 按钮按下时稍微向下移动，模拟按下的感觉 */
        }
      }
    }
  }
}
</style>
