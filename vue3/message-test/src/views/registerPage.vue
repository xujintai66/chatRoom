<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '@/api/register'

const router = useRouter()
const account = ref('')
const passWord = ref('')
const username = ref('')

const submit = async () => {
  if (account.value === '' || passWord.value === '' || username.value === '') {
    alert('三个框都要输入哦')
    return
  }
  const Data = {
    account: account.value,
    passWord: passWord.value,
    username: username.value,
  }
  const { data } = await register(Data)
  if (data.code === 401) {
    alert(data.message)
    account.value === ''
    return
  } else {
    alert(data.message)
    router.push('/login')
  }
}
</script>

<template>
  <div class="container">
    <div class="main">
      <div class="top">
        <div class="topImg">
          <img src="../assets/image/register.png" alt="" />
        </div>
        <div class="topText">注册</div>
      </div>
      <div class="inputCss">
        <div class="inputLeft">账号</div>
        <input
          type="text"
          v-model="account"
          class="inputRight"
          placeholder="输入账号且少于20个字符"
        />
      </div>
      <div class="inputCss">
        <div class="inputLeft">密码</div>
        <input
          type="text"
          v-model="passWord"
          class="inputRight"
          placeholder="输入密码且少于20个字符"
        />
      </div>
      <div class="inputCss">
        <div class="inputLeft">用户昵称</div>
        <input
          type="text"
          v-model="username"
          class="inputRight"
          placeholder="输入你想要使用的名称"
        />
      </div>
      <div class="bottomPart">
        <div class="register" @click="submit">
          <div>注册</div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 97vh;
  .main {
    width: 500px;
    height: 500px;
    margin: 0 auto;
    border: 1px solid #000;
    display: flex;
    flex-direction: column;
    .top {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 100%;
      height: 100px;
      .topImg {
        width: 50px;
        height: 50px;
        img {
          width: 100%;
          height: 100%;
        }
      }
      .topText {
        width: 300px;
        height: 50px;
        font-size: 36px;
        text-align: center;
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
    .bottomPart {
      width: 100%;
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
        margin: 0 auto;
        &:active {
          background-color: green;
          box-shadow: 0 0 3px black;
          transform: translateY(1px);
        }
      }
    }
  }
}
</style>
