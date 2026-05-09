# AVL Tree & BST Concept Map

```mermaid
graph TD
    A[자료 구조] --> B(트리)
    B --> C(이진 트리)
    C --> D(이진 탐색 트리 - BST)

    D -- 속성 --> D1{왼쪽 자식 < 부모 < 오른쪽 자식}
    D -- 연산 --> D2{삽입, 삭제, 검색}
    D -- 순회 --> D3{중위 순회: 정렬된 결과}
    D -- 시간 복잡도 --> D4{평균: O(log n), 최악: O(n)}
    D -- 문제점 --> D5[편향 트리 시 성능 저하]

    D5 --> E(AVL 트리)
    E -- 해결책 --> E1{자가 균형 이진 탐색 트리}

    E -- 핵심 개념 --> E2{밸런스 팩터}
    E2 -- 정의 --> E2_1{height(왼쪽) - height(오른쪽)}
    E2 -- 범위 --> E2_2{-1, 0, 1}

    E -- 핵심 연산 --> E3{회전 (Rotation)}
    E3 -- 유형 --> E3_1{LL (Right Rotate)}
    E3_1 -- 조건 --> E3_1_1{balance > 1 && key < left->key}
    E3 -- 유형 --> E3_2{RR (Left Rotate)}
    E3_2 -- 조건 --> E3_2_1{balance < -1 && key > right->key}
    E3 -- 유형 --> E3_3{LR (Left-Right Rotate)}
    E3_3 -- 조건 --> E3_3_1{balance > 1 && key > left->key}
    E3 -- 유형 --> E3_4{RL (Right-Left Rotate)}
    E3_4 -- 조건 --> E3_4_1{balance < -1 && key < right->key}

    E3 -- 중요 --> E4{회전 후 노드 높이 업데이트 필수}
    E -- 결과 --> E5{높이 O(log n) 보장}

    subgraph C++ 구현
        F[Node 구조체] --> F1{key, height, left, right 포인터}
        G[getHeight(Node*)] --> G1{nullptr 체크}
        H[getBalance(Node*)] --> H1{left - right 일관성}
        I[rightRotate(Node*)] --> I1{높이 업데이트}
        J[leftRotate(Node*)] --> J1{높이 업데이트}
        K[insert(Node*, int)] --> K1{재귀 호출}
        K1 --> K2{높이 업데이트}
        K2 --> K3{getBalance 호출}
        K3 --> E3{회전 로직 적용}
    end
```
