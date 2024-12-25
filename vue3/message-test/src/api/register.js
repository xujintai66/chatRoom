import request from '@/utils/request'
export const register = (data) => {
  return request.post('/register', JSON.stringify(data))
}
