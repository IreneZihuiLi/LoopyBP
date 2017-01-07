# From: http://wuchong.me/blog/2014/02/09/algorithm-sort-summary/

# TODO: Merge Sort
def merge_sort(ary):
    if len(ary) <= 1 : return ary
    num = int(len(ary)/2)       #二分分解
    left = merge_sort(ary[:num])
    right = merge_sort(ary[num:])
    return merge(left,right)    #合并数组

def merge(left,right):
    '''合并操作，
    将两个有序数组left[]和right[]合并成一个大的有序数组'''
    l,r = 0,0           #left与right数组的下标指针
    result = []
    while l<len(left) and r<len(right) :
        if left[l] < right[r]:
            result.append(left[l])
            l += 1
        else:
            result.append(right[r])
            r += 1
    result += left[l:]
    result += right[r:]
    return result


# TODO: Bubble Sort
def bubble_sort(array):
    n = len(array)
    if n <= 1: return array
    for i in range(n):
        for j in range(n-i-1):
            if array[j + 1] < array[j]:  # 如果前者比后者大
                array[j + 1], array[j] = array[j], array[j + 1]  # 则交换两者
    return array


# TODO: Selection Sort
def select_sort(array):
    n = len(array)
    if n <= 1: return array
    for i in range(n):
        max = array[i]
        flag = i
        for j in range(1+i,n):
            if array[j]<max:flag = j
        array[i],array[flag] = array[flag],array[i] #swap
    return array

# TODO: Insertion Sort: 对于每个未排序数据，在已排序序列中从后向前扫描，找到相应位置并插入。
def insert_sort(alist):
    for index in range(1,len(alist)):

        currentvalue = alist[index]
        position = index

        while position>0 and alist[position-1]>currentvalue:
             alist[position]=alist[position-1]
             position = position-1

        alist[position]=currentvalue
    return alist


# TODO: Shell Sort:将数组列在一个表中并对列分别进行插入排序，
# 重复这过程，不过每次用更长的列（步长更长了，列数更少了）来进行。
# 最后整个表就只有一列了。将数组转换至表是为了更好地理解这算法，
# 算法本身还是使用数组进行排序。
def shell_sort(ary):
    n = len(ary)
    gap = round(n/2)       #初始步长 , 用round四舍五入取整
    while gap > 0 :
        for i in range(gap,n):        #每一列进行插入排序 , 从gap 到 n-1
            temp = ary[i]
            j = i
            while ( j >= gap and ary[j-gap] > temp ):    #插入排序
                ary[j] = ary[j-gap]
                j = j - gap
            ary[j] = temp
        gap = round(gap/2)                     #重新设置步长
    return ary


# TODO: Quick Sort:将数组列在一个表中并对列分别进行插入排序
def quick_sort(array):
    return qsort(array,0,len(array)-1)

def qsort(arrary, left, right):
    #快排函数，array为待排序数组，left为待排序的左边界，right为右边界
    if left >= right : return arrary
    key = arrary[left]     #取最左边的为基准数
    lp = left           #左指针
    rp = right          #右指针
    while lp < rp :
        while arrary[rp] >= key and lp < rp : # 右边的元素大于key,继续移动(<-)
            rp -= 1
        while arrary[lp] <= key and lp < rp : # 左边的元素小于key,继续移动(->)
            lp += 1
        arrary[lp], arrary[rp] = arrary[rp], arrary[lp] # 左右的元素换
    arrary[left], arrary[lp] = arrary[lp], arrary[left] # key 放到数组中间去
    qsort(arrary, left, lp - 1)
    qsort(arrary, rp + 1, right)
    return arrary


# TODO: Heap Sort:堆排序在 top K 问题中使用比较频繁。
# 堆排序是采用二叉堆的数据结构来实现的，虽然实质上还是一维数组。
# 二叉堆是一个近似完全二叉树
def heap_sort(ary) :
    n = len(ary)
    first = int(n/2-1)       #最后一个非叶子节点
    for start in range(first,-1,-1) :     #构造大根堆
        max_heapify(ary,start,n-1)
    for end in range(n-1,0,-1):           #堆排，将大根堆转换成有序数组
        ary[end],ary[0] = ary[0],ary[end]
        max_heapify(ary,0,end-1)
    return ary


#最大堆调整：将堆的末端子节点作调整，使得子节点永远小于父节点
#start为当前需要调整最大堆的位置，end为调整边界
def max_heapify(ary,start,end):
    root = start
    while True :
        child = root*2 +1               #调整节点的子节点
        if child > end : break
        if child+1 <= end and ary[child] < ary[child+1] :
            child = child+1             #取较大的子节点
        if ary[root] < ary[child] :     #较大的子节点成为父节点
            ary[root],ary[child] = ary[child],ary[root]     #交换
            root = child
        else :
            break





test = [7,3,4,9,5,2,6,1]
print ("Test sort {}".format(test))
# print ("Merge sort {}".format(merge_sort(test)))
# print ("Bubble sort {}".format(bubble_sort(test)))
# print ("Selection sort {}".format(select_sort(test)))
# print ("Insertion sort {}".format(insert_sort(test)))
# print ("Shell sort {}".format(shell_sort(test)))
# print ("Quick sort {}".format(quick_sort(test)))
print ("Heap sort {}".format(heap_sort(test)))

