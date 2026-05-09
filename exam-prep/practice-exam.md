# 예상 문제 (BST 및 AVL 트리)

## 문제 1: 이진 탐색 트리의 개념 및 시간 복잡도 (서술형)

이진 탐색 트리(BST)의 핵심 속성을 설명하고, 삽입, 삭제, 탐색 연산에 대한 평균 및 최악의 시간 복잡도를 서술하시오. 또한, BST가 최악의 성능을 보이는 "편향 트리(skewed tree)"의 개념과 발생 가능한 시나리오에 대해 설명하시오.

---

## 문제 2: AVL 트리의 균형 유지 및 회전 (코드 분석 및 개선)

다음은 AVL 트리의 `getBalance` 함수와 `rightRotate` 함수 일부입니다. 주어진 코드의 문제점을 지적하고, 이를 해결하기 위한 C++ 코드 수정안을 제시하시오. `getHeight` 함수는 올바르게 구현되어 있다고 가정합니다.

**제공 코드:**

```cpp
// getBalance 함수
int getBalance(Node* node) {
    if (node == nullptr) return 0;
    return getHeight(node->right) - getHeight(node->left); // 문제점 1: 일반적인 밸런스 팩터 정의와 다름
}

// rightRotate 함수 (부분)
Node* rightRotate(Node* y) {
    Node* x = y->left;
    Node* T2 = x->right;

    x->right = y;
    y->left = T2;

    // 문제점 2: 회전 후 노드의 높이 업데이트 누락

    return x;
}
```

**요구 사항:**
1.  `getBalance` 함수에서 흔히 사용되는 `height(left) - height(right)` 방식으로 변경했을 때의 코드와 그 이유를 설명하시오.
2.  `rightRotate` 함수에서 누락된 높이 업데이트 로직을 추가한 코드를 제시하시오.

---

## 문제 3: AVL 트리의 삽입 및 회전 시뮬레이션 (객관식/주관식 혼합)

다음과 같은 순서로 정수를 빈 AVL 트리에 삽입할 때, 각 삽입 작업 후 트리의 밸런스 팩터(BF)가 불균형 상태가 되어 회전이 필요한 시점과, 그때 수행되는 회전 유형(LL, RR, LR, RL)을 올바르게 연결하시오. (단, 밸런스 팩터는 `height(left) - height(right)`로 계산하며, 삽입된 노드의 키 값이 불균형의 기준이 됩니다.)

**삽입 순서:** 10, 20, 30, 25

**보기:**
a) 10 삽입 후:
b) 20 삽입 후:
c) 30 삽입 후:
d) 25 삽입 후:

**연결할 회전 유형:**
i) RR 회전
ii) LR 회전
iii) RL 회전
iv) LL 회전
v) 회전 불필요

**예시 답변 형식:**
a) - v)
b) - i)
...
