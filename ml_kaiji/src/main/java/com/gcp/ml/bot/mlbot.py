import tensorflow as tf
import numpy as np
from collections import deque
from typing import List
import random
from tensorflow.python.tools import freeze_graph
from tensorflow.python.tools import optimize_for_inference_lib
from tensorflow.python.framework import graph_util

# https://omid.al/posts/2017-02-20-Tutorial-Build-Your-First-Tensorflow-Android-App.html
# https://tensorflow.github.io/serving/serving_basic.html

REPLAY_MEMORY = 50000
BATCH_SIZE = 64
TARGET_UPDATE_FREQUENCY = 5
DISCOUNT_RATE = 1

INPUT_SIZE = 9
OUTPUT_SIZE = 3
MAX_EPISODES = 10000

class Game:    
    def __init__(self):
        self.initGame()    
        
    def initGame(self):
        #print("### init Game ###")
        self.count = 0
        self.done = 0
        
        self.myPoint = 0
        self.emPoint = 0

        self.myDeck = [3, 3, 3]
        self.emDeck = [3, 3, 3]
        
    def getMyRandomAction(self):
        i = 0
        while True:
            i = np.random.randint(3)
            if self.myDeck[i] > 0:
                break;
        return i
        
    def printState(self):
        #print("myDeck:", self.myDeck, "emDeck: ", self.emDeck,"count:", self.count, "myPoint:", self.myPoint, "emPoint:", self.emPoint, "done:",self.done)
        print(np.hstack([self.count, self.myPoint, self.emPoint, self.myDeck, self.emDeck]))
        
    def getState(self):
        return np.hstack([self.count, self.myPoint, self.emPoint, self.myDeck, self.emDeck])

    def doGame(self, action):
        reward = 0.1                  
        if self.done:
            self.initGame()

        if action < 0 or action >= 3:
            self.done = True
            #print ("### oops! wrong play ###", action, self.myDeck)
            reward = -100000
            return reward, self.done

        if self.myDeck[action] <= 0:
            reward = -100000
            self.done = True
            #print ("### oops! wrong play ###", action, self.myDeck)
            return reward, self.done
        else:
            self.myDeck[action] -= 1

        i = 0
        while True:
            i = np.random.randint(3)
            if self.emDeck[i] > 0:
                self.emDeck[i] -= 1
                break

        # 0 = 가위, 1 = 바위, 2 = 보
        if action ==0:
            if i == 0:
                reward = 1
            elif i == 1:
                reward = -10
            elif i == 2:
                reward = 10

        elif action ==1:
            if i == 0:
                reward = 10
            elif i == 1:
                reward = 1
            elif i == 2:
                reward = -10

        elif action ==2:
            if i == 0:
                reward = -10
            elif i == 1:
                reward = 10
            elif i == 2:
                reward = 1

        self.count += 1

        if(self.count >= 9):
            self.done = True

        if(reward < 0):
            self.emPoint += -1* reward
        else:
            self.myPoint += reward;

        return reward, self.done
    
class DQN:
    def __init__(self, session, input_size, output_size, name="main"):
        self.session = session
        self.input_size = input_size
        self.output_size = output_size
        self.net_name = name
        
        self._build_network()
        
    def _build_network(self, h_size=25, l_rate=0.00001):
        with tf.variable_scope(self.net_name):
            self._X = tf.placeholder(tf.float32, [None, self.input_size], name="input_x")
            net = self._X
            #net = tf.layers.dense(net, self.input_size, activation=tf.nn.relu)
            net = tf.layers.dense(net, h_size, activation=tf.nn.relu)
            net = tf.layers.dense(net, h_size, activation=tf.nn.relu)
            net = tf.layers.dense(net, h_size, activation=tf.nn.relu)
            net = tf.layers.dense(net, h_size, activation=tf.nn.relu)
            net = tf.layers.dense(net, h_size, activation=tf.nn.relu)
            #net = tf.layers.dense(net, h_size, activation=tf.nn.relu)
            net = tf.layers.dense(net, self.output_size)
            #net = tf.contrib.layers.softmax(net, name="output_y")
            net = tf.nn.softmax(net, name="output_y")
            self._Qpred = net
            
            self._Y = tf.placeholder(tf.float32, shape=[None, self.output_size])
            self._loss = tf.losses.mean_squared_error(self._Y, self._Qpred)
            optimizer = tf.train.AdamOptimizer(learning_rate=l_rate)
            self._train = optimizer.minimize(self._loss)
            
    def predict(self, state) :
        x = np.reshape(state, [-1, self.input_size])
        return self.session.run(self._Qpred, feed_dict={self._X: x})
    
    def update(self, x_stack, y_stack):
        feed = {self._X: x_stack, self._Y: y_stack}
        return self.session.run([self._loss, self._train], feed)
    
def replay_train(mainDQN, targetDQN, train_batch):
    states = np.vstack([x[0] for x in train_batch])
    actions = np.array([x[1] for x in train_batch])
    rewards = np.array([x[2] for x in train_batch])
    next_states = np.vstack([x[3] for x in train_batch])
    done = np.array([x[4] for x in train_batch])
    
    X = states
    
    Q_target = rewards + DISCOUNT_RATE * np.max(targetDQN.predict(next_states), axis=1) * ~done
    y =mainDQN.predict(states)
    y[np.arange(len(X)), actions] = Q_target
    return mainDQN.update(X, y)

def get_copy_var_ops( dest_scope_name, src_scope_name):
    op_holder  = []
    src_vars = tf.get_collection(tf.GraphKeys.TRAINABLE_VARIABLES, scope=src_scope_name)
    dest_vars = tf.get_collection(tf.GraphKeys.TRAINABLE_VARIABLES, scope=dest_scope_name)
    
    for src_var, dest_var in zip(src_vars, dest_vars):
        op_holder.append(dest_var.assign(src_var.value()))
        
    return op_holder

def bot_play(mainDQN, env):
    
    env.initGame()
    env.printState()
    state = env.getState()
    reward_sum = 0
    
    while True:
        action = np.argmax(mainDQN.predict(state))
        reward, done = env.doGame(action)
        state = env.getState()
        
        reward_sum += reward
        
        if done:
            print("total score: {}".format(reward_sum))
            break
        
def save_model(session, input_tensor, output_tensor):
    signature = tf.saved_model.signature_def_utils.build_signature_def(inputs = {'input': tf.saved_model.utils.build_tensor_info(input_tensor)}, outputs = {'output': tf.saved_model.utils.build_tensor_info(output_tensor)})
    b = tf.saved_model.builder.SavedModelBuilder('/tmp3/model')
    b.add_meta_graph_and_variables(session,[tf.saved_model.tag_constants.SERVING], signature_def_map={tf.saved_model.signature_constants.DEFAULT_SERVING_SIGNATURE_DEF_KEY: signature})
    b.save()
    
def main():
    replay_buffer = deque(maxlen=REPLAY_MEMORY)
    last_100_game_reward = deque(maxlen=100)
    env = Game()
    
    
    
    with tf.Session() as sess:
        mainDQN = DQN(sess, INPUT_SIZE, OUTPUT_SIZE, name="main")
        targetDQN = DQN(sess, INPUT_SIZE, OUTPUT_SIZE, name="target")
        sess.run(tf.global_variables_initializer())
        
        saver = tf.train.Saver()
         
        saver.restore(sess, "tmp/model.ckpt")
        #tf.train.write_graph(sess.graph_def, '.', 'tmp/model.pbtxt')  
        #saver_def = saver.as_saver_def()
        
        
        copy_ops = get_copy_var_ops(dest_scope_name="target", src_scope_name="main")
        weight = sess.run(copy_ops)
        
        win = 0
        lose = 0
        wrong = 0
        
        temp_win = 0
        temp_lose = 0
        temp_wrong = 0
        
        for episode in range(MAX_EPISODES):
            e = 1. / ((episode/10)+1)
            #e = 0
            done = False
            step_count = 0
            env.initGame()
            state = env.getState()
            reward_sum = 0
            
            while not done:
                if np.random.rand() < e:
                    action = env.getMyRandomAction()
                else:
                    predict = mainDQN.predict(state)
                    #if episode % 1000 == 0:
                    #    print("predict:", predict)
                    action = np.argmax(predict)
                    
                reward, done = env.doGame(action)
                next_state = env.getState()
                
                if done :
                    
                    
                    if env.count < 9:
                        wrong += 1
                        temp_wrong += 1
                    else:
                        
                        if env.myPoint > env.emPoint:
                            reward = 100
                            win += 1
                            temp_win += 1
                        elif env.emPoint > env.myPoint:
                            lose += 1
                            temp_lose += 1
                            reward -= 10
                            
                    if episode % 1000 == 0:
                        env.printState()
                        print("episode:", episode, "reward:", reward, "win:", temp_win, "lose:", temp_lose, "wrong:", temp_wrong)
                        temp_win = 0
                        temp_lose = 0
                        temp_wrong = 0
                        saver.save(sess, "tmp/model.ckpt")
                        #save_model(sess, mainDQN._X, mainDQN._Qpred )
                        #freeze_graph()
                        #saver.save(sess, 'tmp/trained_model.sd')
                        #tf.train.write_graph(sess.graph_def, '.', 'tmp/trained_model.proto', as_text=False)
                        #tf.train.write_graph(sess.graph_def, '.', 'tmp/trained_model.txt', as_text=True)
                        
                reward_sum += reward
                replay_buffer.append((state, action, reward, next_state, done))
                

                if len(replay_buffer) > BATCH_SIZE:
                    minibatch = random.sample(replay_buffer, BATCH_SIZE)
                    loss, _ = replay_train(mainDQN, targetDQN, minibatch)
                    
                if step_count % TARGET_UPDATE_FREQUENCY == 0:
                    weight = sess.run(copy_ops)
                    #if episode % 10000 == 0:
                    #    print(weight)
                    
                    
                state = next_state
                step_count += 1
                
            #print("Episode: {}  steps: {}".format(episode, step_count))
            
            # CartPole-v0 Game Clear Checking Logic
            last_100_game_reward.append(reward_sum)

            if len(last_100_game_reward) == last_100_game_reward.maxlen:
                avg_reward = np.mean(last_100_game_reward)

                 #print(f"Game Cleared in {episode} episodes with avg reward {avg_reward}")
        #print(sess.run(copy_ops))
        print("win:" , win)
        print("lose:", lose)
        print("wrong:", wrong)
        
        saver.save(sess, "tmp/model.ckpt")
        save_model(sess, mainDQN._X, mainDQN._Qpred )
        #freeze_graph()
        #saver.save(sess, 'tmp/trained_model.sd')
        #tf.train.write_graph(sess.graph_def, '.', 'tmp/trained_model.proto', as_text=False)
        #tf.train.write_graph(sess.graph_def, '.', 'tmp/trained_model.txt', as_text=True)
        
        
        
main() 