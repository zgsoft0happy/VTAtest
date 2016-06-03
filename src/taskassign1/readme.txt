本包里的代码主要是用于任务分配过程中的校验，校验过程中的genChallenge、genProof、verify等步骤完全是
用类VerifyUtils类的静态方法实现，具体参数可参考生成的API文档。任务刚分配时，是List<VeirfyBlock>的
样式。并通过转变，转变为Map<DataOwner , List<VerifyBlock>>的样式，注意，这里的功能设计有缺陷，因为
同一次任务分配中，某一用户的数据块必须来自于同一文件。测试时请注意。