Recursion
---------

https://gist.github.com/prayagupd/51fc83a16b394d2586c2#file-5_recursion-md

- pascal triangle, easy, https://leetcode.com/problems/pascals-triangle/description/

- balance parenthesis, easy, https://leetcode.com/problems/valid-parentheses/description/

- change coins, medium, https://leetcode.com/problems/coin-change/description/

```

[4, (2)]                                  +  [3, (1, 2)]
[4, ()]        +    [2, (2)]
             [2, ()]    +   [0, (2)]
         
                                       [3, (2)]                     + [2, (1, 2)]
                                 [3, ()] -> 0 + [1, (2)]
                                       [1, List()]+ [-1,List(2)]
                                                             [2, (2)]        +               [1, (1, 2)]
                                                        [2, ()]    +   [0, (2)]
                                                                                            [1, (2)]       +       [0, (1, 2)]
                                                                                       [1, ()]  +   [-1, (2)]

```
