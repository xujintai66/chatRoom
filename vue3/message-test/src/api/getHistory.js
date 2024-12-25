import request from '@/utils/request'
export const getHistory = () => {
  return request.get('/history')
}
