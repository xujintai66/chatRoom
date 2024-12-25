import request from '@/utils/request'
export const update = (data) => {
  return request.post('/update', JSON.stringify(data))
}
