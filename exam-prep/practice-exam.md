# AVL Tree & BST Practice Exam

## 예상 문제 1: AVL 트리의 균형 인자(Balance Factor) 구현

**문제:**
다음은 `getBalance` 함수를 구현한 C++ 코드입니다. 이 코드가 일반적인 AVL 트리의 균형 인자 계산 방식과 다를 경우, 어떤 문제가 발생할 수 있는지 설명하고, 이를 표준 방식(`height(left) - height(right)`)으로 수정하시오.

```cpp
int getBalance(Node* node) {
    if (node == nullptr) return 0;
    return getHeight(node->right) - getHeight(node->left);
}
```

**풀이:**
- **문제점**: 일반적으로 균형 인자는 `height(왼쪽 서브트리) - height(오른쪽 서브트리)`로 계산합니다. 위 코드는 이와 반대로 계산하므로, 삽입 또는 삭제 후 트리의 균형을 맞추기 위한 회전(rotation) 조건을 설정할 때 혼동을 야기하거나 잘못된 회전이 발생할 수 있습니다 (예: `balance > 1`이 왼쪽이 아닌 오른쪽 서브트리가 높은 상황을 의미하게 됨).
- **수정 코드 (C++):**
```cpp
int getBalance(Node* node) {
    if (node == nullptr) return 0;
    // 표준 방식: height(left) - height(right)
    return getHeight(node->left) - getHeight(node->right);
}
```

--- 

## 예상 문제 2: AVL 트리 회전 후 높이 업데이트의 중요성

**문제:**
AVL 트리에서 `rightRotate` 함수를 구현할 때, 회전이 완료된 후 관련 노드들의 높이(height)를 업데이트해야 합니다. 만약 높이 업데이트가 누락된다면 어떤 문제가 발생할 수 있으며, 이를 수정하는 C++ 코드를 작성하시오.

**풀이:**
- **문제점**: 회전은 트리의 구조를 변경하므로, 회전된 노드(여기서는 `y`와 `x`)의 높이도 함께 변경됩니다. 높이 업데이트가 누락되면, 다음 삽입/삭제 연산 시 `getBalance` 함수가 잘못된 높이를 참조하여 부정확한 균형 인자를 반환하게 됩니다. 이는 트리가 올바르게 균형을 유지하지 못하고 AVL 트리의 중요한 속성(높이 O(log n))을 위반하게 만들 수 있습니다.
- **수정 코드 (C++):**
```cpp
Node* rightRotate(Node* y) {
    Node* x = y->left;
    Node* T2 = x->right;

    x->right = y;
    y->left = T2;

    // 높이 업데이트 추가: 자식 노드부터 역순으로 업데이트하는 것이 일반적
    y->height = 1 + max(getHeight(y->left), getHeight(y->right));
    x->height = 1 + max(getHeight(x->left), getHeight(x->right));

    return x;
}
```

--- 

## 예상 문제 3: AVL 트리의 `insert` 함수 내 LL 회전 구현

**문제:**
AVL 트리의 `insert` 함수에서 노드를 삽입한 후, 균형 인자를 확인하여 필요한 경우 회전을 수행합니다. 균형 인자 계산 방식이 `height(left) - height(right)`라고 가정할 때, LL (Left-Left) 케이스에 해당하는 회전 조건을 설명하고, 이를 처리하는 `insert` 함수의 C++ 코드를 작성하시오.

**풀이:**
- **LL 케이스 조건**: 
    1. 현재 노드의 균형 인자가 `> 1` (왼쪽 서브트리가 높다는 의미).
    2. 새로 삽입된 키(`key`)가 현재 노드의 왼쪽 자식의 왼쪽 서브트리에 삽입되어 균형을 깨뜨린 경우 (`key < node->left->key`).
- **처리**: 현재 노드에 대해 `rightRotate`를 수행하여 균형을 맞춥니다.
- **`insert` 함수 내 LL 케이스 처리 코드 (C++):**
```cpp
Node* insert(Node* node, int key) {
    // ... (삽입 로직 및 높이 업데이트 생략)
    // if (node == nullptr) return new Node(key);
    // ... (key < node->key 또는 key > node->key 에 따라 재귀 호출)
    // node->height = 1 + max(getHeight(node->left), getHeight(node->right));

    int balance = getBalance(node); // getBalance는 height(left) - height(right) 가정

    // LL Case: 왼쪽 서브트리가 높고, 새 노드가 왼쪽 자식의 왼쪽에 삽입됨
    if (balance > 1 && key < node->left->key)
        return rightRotate(node);

    // ... (RR, LR, RL 케이스 생략)

    return node;
}
```
